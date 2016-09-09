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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.fernandocejas.arrow.optional.Optional;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatter;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatters;
import com.wlobs.wilqor.mobile.activity.map.MapConstants;
import com.wlobs.wilqor.mobile.activity.map.WorkaroundMapFragment;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtilities;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.persistence.model.Observation;
import com.wlobs.wilqor.mobile.persistence.model.Vote;
import com.wlobs.wilqor.mobile.persistence.model.Vote_Table;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ObservationDetailsActivity extends NavigationActivity implements OnMapReadyCallback {
    public static final String INTENT_OBSERVATION_KEY = "OBSERVATION_IN_INTENT";

    @BindView(R.id.observation_details_date)
    TextView date;

    @BindView(R.id.observation_details_author_container)
    View authorContainer;

    @BindView(R.id.observation_details_author)
    TextView author;

    @BindView(R.id.observation_details_species_class)
    TextView speciesClass;

    @BindView(R.id.observation_details_species)
    TextView species;

    @BindView(R.id.observation_details_votes_number)
    TextView votesNumber;

    @BindView(R.id.observation_details_restricted_container)
    View restrictedContainer;

    @BindView(R.id.observation_details_cast_vote_button)
    Button castVoteButton;

    @BindView(R.id.observation_details_delete_button)
    Button deleteObservationButton;

    @BindView(R.id.observation_details_restriction_switch)
    Switch restrictionSwitch;

    @BindView(R.id.drawer_layout)
    View topLevelLayout;

    @BindView(R.id.observation_details_scroll_view)
    ScrollView scrollView;

    private AuthUtility authUtility;

    private DateFormatter dateFormatter;
    private Observation currentObservation;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_observation_details, contentFrameLayout);
        ButterKnife.bind(this);
        dateFormatter = DateFormatters.getFullDateFormatter();
        authUtility = AuthUtilities.getAuthUtility(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.observation_details_google_map);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.observation_details_google_map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        currentObservation = (Observation) intent.getSerializableExtra(INTENT_OBSERVATION_KEY);
        if (currentObservation != null) {
            String login = authUtility.getLogin().get();
            boolean mineObservation = login.equals(currentObservation.getAuthor());
            if (mineObservation) {
                deleteObservationButton.setVisibility(View.VISIBLE);
                castVoteButton.setVisibility(View.GONE);
                authorContainer.setVisibility(View.GONE);
                restrictedContainer.setVisibility(View.VISIBLE);
            } else {
                Optional<Vote> castedVote = Optional.fromNullable(
                        SQLite.select()
                                .from(Vote.class)
                                .where(Vote_Table.remoteObservationId.eq(currentObservation.getRemoteId()))
                                .and(Vote_Table.voter.eq(login))
                                .querySingle());
                boolean voteAlreadyCasted = castedVote.isPresent();
                castVoteButton.setClickable(!voteAlreadyCasted);
                deleteObservationButton.setVisibility(View.GONE);
                castVoteButton.setVisibility(View.VISIBLE);
                authorContainer.setVisibility(View.VISIBLE);
                restrictedContainer.setVisibility(View.GONE);
            }
            author.setText(currentObservation.getAuthor());
            date.setText(dateFormatter.format(currentObservation.getDateUtcTimestamp()));
            speciesClass.setText(currentObservation.getSpecies().getSpeciesClass().asString());
            species.setText(currentObservation.getSpecies().getName());
            votesNumber.setText(String.valueOf(currentObservation.getVotesCount()));
            restrictionSwitch.setChecked(currentObservation.isRestricted());
        } else {
            finish();
        }
    }

    @OnCheckedChanged(R.id.observation_details_restriction_switch)
    public void handleRestrictionSwitchCheck(boolean checked) {
        if (currentObservation != null) {
            currentObservation.setRestricted(checked);
            currentObservation.setRestrictionChanged(true);
            currentObservation.update();
        }
    }

    @OnClick(R.id.observation_details_cast_vote_button)
    public void onCastVoteButtonClick() {
        Vote vote = new Vote();
        vote.setRemoteObservationId(currentObservation.getRemoteId());
        vote.setSpecies(currentObservation.getSpecies());
        vote.setObservationOwner(currentObservation.getAuthor());
        vote.setDateUtcTimestamp(new Date().getTime());
        vote.setVoter(authUtility.getLogin().get());
        vote.save();
        castVoteButton.setClickable(false);
        Snackbar.make(topLevelLayout, getString(R.string.vote_details_vote_confirmation_label), Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.observation_details_delete_button)
    public void onDeleteObservationButtonClick() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.observation_details_delete_confirmation)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentObservation.setDeleted(true);
                        currentObservation.update();
                        ObservationDetailsActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        LatLng position = new LatLng(currentObservation.getLatitude(), currentObservation.getLongitude());
        map.addMarker(new MarkerOptions().position(position));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, MapConstants.DETAILS_ZOOM_LEVEL));
    }
}
