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

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fernandocejas.arrow.optional.Optional;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatter;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatters;
import com.wlobs.wilqor.mobile.activity.map.WorkaroundMapFragment;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtilities;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.persistence.model.Observation;
import com.wlobs.wilqor.mobile.persistence.model.Species;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ObservationCreationActivity extends NavigationActivity implements
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final int LOCATION_REQUEST_INTERVAL = 10000;
    private static final int LOCATION_REQUEST_FASTEST_INTERVAL = 1000;

    @BindView(R.id.observation_creation_date_label)
    TextView dateLabel;

    @BindView(R.id.observation_creation_scroll_view)
    ScrollView scrollView;

    @BindView(R.id.observations_creation_top_level_layout)
    View topLevelLayout;

    SpeciesSelectionFragment speciesSelectionFragment;

    private AuthUtility authUtility;
    private DateFormatter dateFormatter;
    private SlideDateTimeListener dateTimeListener;

    // selected observation parameters below
    private boolean restricted;
    private Date date;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Optional<LatLng> locationOptional;
    private Optional<Marker> locationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_observation_creation, contentFrameLayout);
        ButterKnife.bind(this);

        authUtility = AuthUtilities.getAuthUtility(getApplicationContext());
        dateFormatter = DateFormatters.getFullDateFormatter();
        dateTimeListener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                Date now = new Date();
                // the widget allows for selecting hour/minute from the future,
                // which is why the result has to be cropped to the current time
                ObservationCreationActivity.this.date = date.getTime() <= now.getTime() ? date : now;
                ObservationCreationActivity.this.updateDateLabelContent();
            }
        };
        speciesSelectionFragment = (SpeciesSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.observation_creation_species_fragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.observations_creation_google_map);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.observations_creation_google_map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        mapFragment.getMapAsync(this);
        locationOptional = Optional.absent();
        locationMarker = Optional.absent();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_REQUEST_INTERVAL)
                .setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.date = new Date();
        updateDateLabelContent();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @OnClick(R.id.observation_creation_date_button)
    public void onDateButtonClick() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(dateTimeListener)
                .setInitialDate(date)
                .setMaxDate(new Date())
                .setIs24HourTime(true)
                .build()
                .show();
    }

    @OnCheckedChanged(R.id.observation_creation_restriction_switch)
    public void handleRestrictionSwitchCheck(boolean checked) {
        restricted = checked;
    }

    @OnClick(R.id.observation_creation_save_button)
    public void onSaveButtonClick() {
        if (!locationOptional.isPresent()) {
            showNoLocationSetSnackbar();
            return;
        }
        LatLng setPosition = locationOptional.get();
        Optional<Species> validSpecies = speciesSelectionFragment.getValidSpecies();
        if (validSpecies.isPresent()) {
            Observation created = new Observation();
            created.setRestricted(restricted);
            created.setDateUtcTimestamp(date.getTime());
            created.setSpecies(validSpecies.get());
            created.setAuthor(authUtility.getLogin().get());
            created.setLatitude(setPosition.latitude);
            created.setLongitude(setPosition.longitude);
            created.save();
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ObservationCreationActivity.this.updateLocationMarker(latLng);
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        Optional<Location> lastLocation = Optional.fromNullable(LocationServices.FusedLocationApi.getLastLocation(googleApiClient));
        if (lastLocation.isPresent()) {
            handleNewLocation(lastLocation.get());
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        if (!locationOptional.isPresent()) {
            updateLocationMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void updateLocationMarker(LatLng clickLocation) {
        if (locationMarker.isPresent()) {
            locationMarker.get().remove();
        }
        Marker updated = map.addMarker(new MarkerOptions().position(clickLocation));
        map.moveCamera(CameraUpdateFactory.newLatLng(clickLocation));
        locationMarker = Optional.of(updated);
        locationOptional = Optional.of(clickLocation);
    }

    private void updateDateLabelContent() {
        dateLabel.setText(dateFormatter.format(this.date.getTime()));
    }

    private void showNoLocationSetSnackbar() {
        Snackbar.make(topLevelLayout, getString(R.string.observation_creation_empty_location_error), Snackbar.LENGTH_LONG).show();
    }

}
