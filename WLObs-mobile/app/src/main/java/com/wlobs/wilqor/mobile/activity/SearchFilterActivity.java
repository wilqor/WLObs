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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fernandocejas.arrow.optional.Optional;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatter;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatters;
import com.wlobs.wilqor.mobile.activity.model.SearchFilter;
import com.wlobs.wilqor.mobile.activity.model.SpeciesSelection;
import com.wlobs.wilqor.mobile.persistence.model.Species;
import com.wlobs.wilqor.mobile.rest.model.AggregationRequestDto;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SearchFilterActivity extends NavigationActivity {
    private static final long SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000;
    static final String INTENT_FILTER_PARAMETER = "FILTER_PARAMETER";

    @BindView(R.id.search_filter_time_checkbox)
    CheckBox timeCheckbox;

    @BindView(R.id.search_filter_time_section)
    View timeSection;

    @BindView(R.id.search_filter_date_from_label)
    TextView dateFromLabel;

    @BindView(R.id.search_filter_date_to_label)
    TextView dateToLabel;

    @BindView(R.id.search_filter_species_checkbox)
    CheckBox speciesCheckbox;

    @BindView(R.id.search_filter_species_section)
    View speciesSection;

    SpeciesSelectionFragment speciesSelectionFragment;

    private DateFormatter dateFormatter;
    private boolean timeSectionShown;
    private boolean speciesSectionShown;
    private Date dateFrom;
    private Date dateTo;
    private SlideDateTimeListener dateFromListener;
    private SlideDateTimeListener dateToListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_search_filter, contentFrameLayout);
        ButterKnife.bind(this);

        dateFormatter = DateFormatters.getFullDateFormatter();
        speciesSelectionFragment = (SpeciesSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.search_filter_species_fragment);
        dateFromListener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                // crop up to dateTo
                SearchFilterActivity.this.dateFrom = date.getTime() <= dateTo.getTime() ? date : dateTo;
                SearchFilterActivity.this.updateDateFromLabelTextContent();
            }
        };
        dateToListener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                // crop up to now
                Date now = new Date();
                SearchFilterActivity.this.dateTo = date.getTime() <= now.getTime() ? date : now;
                SearchFilterActivity.this.updateDateToLabelTextContent();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        timeSectionShown = true;
        speciesSectionShown = false;
        dateTo = new Date();
        dateFrom = new Date(dateTo.getTime() - SEVEN_DAYS);
        Intent intent = getIntent();
        SearchFilter previousFilter = (SearchFilter) intent.getSerializableExtra(INTENT_FILTER_PARAMETER);
        if (previousFilter != null) {
            Optional<AggregationRequestDto.TimeRange> timeRangeOptional = previousFilter.getTimeRangeOptional();
            Optional<SpeciesSelection> speciesSelectionOptional = previousFilter.getSpeciesSelectionOptional();
            if (timeRangeOptional.isPresent()) {
                AggregationRequestDto.TimeRange previousTimeRange = timeRangeOptional.get();
                timeSectionShown = true;
                dateTo = new Date(previousTimeRange.getDateToUtcTimestamp());
                dateFrom = new Date(previousTimeRange.getDateFromUtcTimestamp());
            } else {
                timeSectionShown = false;
            }
            if (speciesSelectionOptional.isPresent()) {
                SpeciesSelection previousSelection = speciesSelectionOptional.get();
                speciesSectionShown = true;
                speciesSelectionFragment.setSpeciesClass(previousSelection.getSpeciesClass());
                speciesSelectionFragment.setSpeciesName(previousSelection.getSpeciesName());
            }
        }
        timeCheckbox.setChecked(timeSectionShown);
        speciesCheckbox.setChecked(speciesSectionShown);
        updateTimeSectionVisibility();
        updateSpeciesSectionVisibility();
        updateDateFromLabelTextContent();
        updateDateToLabelTextContent();
    }

    @OnCheckedChanged(R.id.search_filter_time_checkbox)
    public void handleTimeCheckboxCheck(boolean checked) {
        timeSectionShown = checked;
        updateTimeSectionVisibility();
    }

    @OnCheckedChanged(R.id.search_filter_species_checkbox)
    public void handleSpeciesCheckboxCheck(boolean checked) {
        speciesSectionShown = checked;
        updateSpeciesSectionVisibility();
    }

    @OnClick(R.id.search_filter_date_from_button)
    public void onDateFromButtonClick() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(dateFromListener)
                .setInitialDate(dateFrom)
                .setMaxDate(new Date())
                .setIs24HourTime(true)
                .build()
                .show();
    }

    @OnClick(R.id.search_filter_date_to_button)
    public void onDateToButtonClick() {
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(dateToListener)
                .setInitialDate(dateTo)
                .setMaxDate(new Date())
                .setIs24HourTime(true)
                .build()
                .show();

    }

    @OnClick(R.id.search_filter_apply_button)
    public void onApplyButtonClick() {
        Optional<AggregationRequestDto.TimeRange> timeRangeOptional = Optional.absent();
        Optional<SpeciesSelection> speciesSelectionOptional = Optional.absent();
        if (timeSectionShown) {
            timeRangeOptional = Optional.of(new AggregationRequestDto.TimeRange(dateFrom.getTime(), dateTo.getTime()));
        }
        if (speciesSectionShown) {
            Optional<Species> validSpecies = speciesSelectionFragment.getValidSpecies();
            // do not accept wrong selection in case this parameter is selected
            if (!validSpecies.isPresent()) {
                return;
            }
            Species selected = validSpecies.get();
            speciesSelectionOptional = Optional.of(new SpeciesSelection(selected.getSpeciesClass(), selected.getName()));
        }
        SearchFilter filter = new SearchFilter(timeRangeOptional, speciesSelectionOptional);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(INTENT_FILTER_PARAMETER, filter);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void updateTimeSectionVisibility() {
        timeSection.setVisibility(timeSectionShown ? View.VISIBLE : View.GONE);
    }

    private void updateSpeciesSectionVisibility() {
        speciesSection.setVisibility(speciesSectionShown ? View.VISIBLE : View.GONE);
    }

    private void updateDateFromLabelTextContent() {
        dateFromLabel.setText(dateFormatter.format(dateFrom.getTime()));
    }

    private void updateDateToLabelTextContent() {
        dateToLabel.setText(dateFormatter.format(dateTo.getTime()));
    }
}
