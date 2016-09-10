/*
 * Copyright 2016 wilqor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wlobs.wilqor.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;

import com.fernandocejas.arrow.optional.Optional;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.model.SearchFilter;
import com.wlobs.wilqor.mobile.activity.model.SpeciesSelection;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtilities;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.persistence.model.Observation;
import com.wlobs.wilqor.mobile.persistence.model.Observation_Table;
import com.wlobs.wilqor.mobile.persistence.model.Species;
import com.wlobs.wilqor.mobile.persistence.model.Species_Table;
import com.wlobs.wilqor.mobile.rest.api.AggregationService;
import com.wlobs.wilqor.mobile.rest.api.RestServices;
import com.wlobs.wilqor.mobile.rest.model.AggregatedObservationDto;
import com.wlobs.wilqor.mobile.rest.model.AggregationRequestDto;
import com.wlobs.wilqor.mobile.rest.model.AggregationResponseDto;
import com.wlobs.wilqor.mobile.rest.model.ExistingObservationDto;
import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends NavigationActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener {
    static final int REQUEST_CODE = 1;
    static final int INSTANT_DELAY_IN_MILLIS = 0;
    static final int CAMERA_MOVE_DELAY_IN_MILLIS = 500;

    @BindView(R.id.search_top_level_layout)
    View topLevelLayout;

    private GoogleMap map;
    private IconGenerator iconGenerator;
    private AggregationService aggregationService;
    private AuthUtility authUtility;
    private Optional<SearchFilter> currentFilter;
    private Optional<Timer> currentRequestTimer;
    private Optional<Call<AggregationResponseDto>> currentAggregationCall;
    private Callback<AggregationResponseDto> aggregationCallback;
    private Map<LatLng, ExistingObservationDto> rawObservationsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_search, contentFrameLayout);
        ButterKnife.bind(this);
        currentFilter = Optional.absent();
        authUtility = AuthUtilities.getAuthUtility(getApplicationContext());
        aggregationService = RestServices.getAggregationService(getApplicationContext(), authUtility);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.search_google_map);
        mapFragment.getMapAsync(this);
        iconGenerator = new IconGenerator(this);
        iconGenerator.setStyle(IconGenerator.STYLE_GREEN);
        rawObservationsMap = new HashMap<>();
        aggregationCallback = new Callback<AggregationResponseDto>() {
            @Override
            public void onResponse(Call<AggregationResponseDto> call, Response<AggregationResponseDto> response) {
                if (response.isSuccessful()) {
                    SearchActivity.this.handleResults(response.body());
                } else {
                    if (response.code() == 401) {
                        SearchActivity.this.authUtility.clearAllTokens();
                        SearchActivity.this.logTheUserOut();
                    } else {
                        SearchActivity.this.showRequestErrorSnackbar();
                    }
                }
            }

            @Override
            public void onFailure(Call<AggregationResponseDto> call, Throwable t) {
                if (!call.isCanceled()) {
                    SearchActivity.this.showRequestErrorSnackbar();
                }
            }
        };
    }

    @OnClick(R.id.search_filter_results_button)
    public void onHandleResultsButtonClick() {
        Intent intent = new Intent(this, SearchFilterActivity.class);
        if (currentFilter.isPresent()) {
            intent.putExtra(SearchFilterActivity.INTENT_FILTER_PARAMETER, currentFilter.get());
        }
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            SearchFilter filter = (SearchFilter) data.getSerializableExtra(SearchFilterActivity.INTENT_FILTER_PARAMETER);
            currentFilter = Optional.fromNullable(filter);
        } else {
            currentFilter = Optional.absent();
        }
        if (isReadyForSearch()) {
            scheduleSearch(INSTANT_DELAY_IN_MILLIS);
        } else {
            showNotReadySnackbar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        currentRequestTimer = Optional.absent();
        currentAggregationCall = Optional.absent();
    }

    @Override
    protected void onPause() {
        super.onPause();

        cancelCurrentSearch();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(this);
        map.setOnCameraChangeListener(this);
        scheduleSearch(INSTANT_DELAY_IN_MILLIS);
    }

    private boolean isReadyForSearch() {
        return map != null;
    }

    private void showNotReadySnackbar() {
        Snackbar.make(topLevelLayout, getString(R.string.search_error_map_not_ready), Snackbar.LENGTH_LONG).show();
    }

    private void showRequestErrorSnackbar() {
        Snackbar.make(topLevelLayout, getString(R.string.search_error_request_failed), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scheduleSearch(INSTANT_DELAY_IN_MILLIS);
                    }
                }).show();
    }

    private void logTheUserOut() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void handleResults(AggregationResponseDto aggregationResponseDto) {
        clearMapState();
        for (AggregatedObservationDto aggregated : aggregationResponseDto.getAggregatedObservations()) {
            addAggregatedObservationMarker(aggregated);
        }
        for (ExistingObservationDto raw : aggregationResponseDto.getRawObservations()) {
            LatLng rawPosition = new LatLng(raw.getLatitude(), raw.getLongitude());
            map.addMarker(new MarkerOptions().position(rawPosition));
            rawObservationsMap.put(rawPosition, raw);
        }
    }

    private void addAggregatedObservationMarker(AggregatedObservationDto aggregatedDto) {
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(String.valueOf(aggregatedDto.getCount()))))
                .position(new LatLng(aggregatedDto.getLatitude(), aggregatedDto.getLongitude()))
                .anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV());
        map.addMarker(markerOptions);
    }

    private void clearMapState() {
        map.clear();
        rawObservationsMap.clear();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (rawObservationsMap.containsKey(marker.getPosition())) {
            ExistingObservationDto observation = rawObservationsMap.get(marker.getPosition());
            Optional<Observation> intentParameter = Optional.absent();
            Optional<Observation> foundLocally = findByRemoteIdAndLogin(observation.getId(), authUtility.getLogin().get());
            // try to match by id and login
            if (foundLocally.isPresent()) {
                intentParameter = Optional.fromNullable(foundLocally.get());
            } else {
                Optional<Species> species = findSpecies(observation.getSpeciesStub());
                if (species.isPresent()) {
                    intentParameter = Optional.of(Observation.fromExistingObservation(observation, species.get()));
                }
            }
            if (intentParameter.isPresent()) {
                Intent intent = new Intent(this, ObservationDetailsActivity.class);
                intent.putExtra(ObservationDetailsActivity.INTENT_OBSERVATION_KEY, intentParameter.get());
                startActivity(intent);
            }
        }
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private Optional<Species> findSpecies(String speciesName) {
        return Optional.fromNullable(SQLite.select()
                .from(Species.class)
                .where(Species_Table.name.eq(speciesName))
                .querySingle());
    }

    private Optional<Species> findSpecies(SpeciesStub speciesStub) {
        return Optional.fromNullable(SQLite.select()
                .from(Species.class)
                .where(Species_Table.speciesClass.eq(speciesStub.getSpeciesClass()))
                .and(Species_Table.latinName.eq(speciesStub.getLatinName()))
                .querySingle());
    }

    private Optional<Observation> findByRemoteIdAndLogin(String remoteId, String login) {
        return Optional.fromNullable(SQLite.select()
                .from(Observation.class)
                .where(Observation_Table.author.eq(login))
                .and(Observation_Table.remoteId.eq(remoteId))
                .querySingle());
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        scheduleSearch(CAMERA_MOVE_DELAY_IN_MILLIS);
    }

    private void scheduleSearch(int delay) {
        cancelCurrentSearch();
        Timer timer = new Timer();
        LatLngBounds currentBounds = map.getProjection().getVisibleRegion().latLngBounds;
        timer.schedule(new RequestTimerTask(currentBounds), delay);
        currentRequestTimer = Optional.of(timer);
    }

    private void cancelCurrentSearch() {
        if (currentAggregationCall.isPresent()) {
            currentAggregationCall.get().cancel();
        }
        if (currentRequestTimer.isPresent()) {
            currentRequestTimer.get().cancel();
        }
    }

    private void sendSearchRequest(LatLngBounds currentBounds) {
        AggregationRequestDto requestDto = new AggregationRequestDto();
        double bottom = currentBounds.southwest.latitude;
        double left = currentBounds.southwest.longitude;
        double top = currentBounds.northeast.latitude;
        double right = currentBounds.northeast.longitude;
        requestDto.setArea(new AggregationRequestDto.Area(bottom, left, top, right));
        if (currentFilter.isPresent()) {
            SearchFilter filter = currentFilter.get();
            if (filter.getTimeRangeOptional().isPresent()) {
                requestDto.setTimeRange(filter.getTimeRangeOptional().get());
            }
            if (filter.getSpeciesSelectionOptional().isPresent()) {
                SpeciesSelection speciesSelection = filter.getSpeciesSelectionOptional().get();
                requestDto.setSpeciesClass(speciesSelection.getSpeciesClass());
                Optional<Species> found = findSpecies(speciesSelection.getSpeciesName());
                if (found.isPresent()) {
                    requestDto.setSpeciesLatinName(found.get().getLatinName());
                }
            }
        }
        Call<AggregationResponseDto> aggregationCall = aggregationService.getAggregatedObservations(requestDto);
        aggregationCall.enqueue(aggregationCallback);
        currentAggregationCall = Optional.of(aggregationCall);
    }

    /**
     * Has to be initialized with the current bounds, as this can only be done within the UI thread.
     */
    private class RequestTimerTask extends TimerTask {
        private final LatLngBounds currentBounds;

        private RequestTimerTask(LatLngBounds currentBounds) {
            this.currentBounds = currentBounds;
        }

        @Override
        public void run() {
            SearchActivity.this.sendSearchRequest(currentBounds);
        }
    }
}
