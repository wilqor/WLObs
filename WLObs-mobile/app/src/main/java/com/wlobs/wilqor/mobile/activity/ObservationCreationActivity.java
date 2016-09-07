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

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fernandocejas.arrow.optional.Optional;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatter;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatters;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtilities;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.persistence.model.Observation;
import com.wlobs.wilqor.mobile.persistence.model.Species;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ObservationCreationActivity extends NavigationActivity {
    @BindView(R.id.observation_creation_date_label)
    TextView dateLabel;

    SpeciesSelectionFragment speciesSelectionFragment;

    private AuthUtility authUtility;
    private DateFormatter dateFormatter;
    private SlideDateTimeListener dateTimeListener;

    // selected observation parameters below
    private boolean restricted;
    private Date date;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.date = new Date();
        updateDateLabelContent();
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
        Optional<Species> validSpecies = speciesSelectionFragment.getValidSpecies();
        if (validSpecies.isPresent()) {
            Observation created = new Observation();
            created.setRestricted(restricted);
            created.setDateUtcTimestamp(date.getTime());
            created.setSpecies(validSpecies.get());
            created.setAuthor(authUtility.getLogin().get());
            // TODO select actual location based on GPS/map
            created.setLatitude(0.0);
            created.setLongitude(0.0);
            created.save();
            finish();
        }
    }

    private void updateDateLabelContent() {
        dateLabel.setText(dateFormatter.format(this.date.getTime()));
    }
}
