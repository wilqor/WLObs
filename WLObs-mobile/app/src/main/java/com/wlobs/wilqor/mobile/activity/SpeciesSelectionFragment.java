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
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.fernandocejas.arrow.optional.Optional;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.adapters.FilterableSpeciesAdapter;
import com.wlobs.wilqor.mobile.activity.adapters.SpeciesClassAdapter;
import com.wlobs.wilqor.mobile.activity.model.SpeciesSelection;
import com.wlobs.wilqor.mobile.activity.validation.InputValidator;
import com.wlobs.wilqor.mobile.activity.validation.InputValidators;
import com.wlobs.wilqor.mobile.activity.validation.ValidationError;
import com.wlobs.wilqor.mobile.persistence.model.Species;
import com.wlobs.wilqor.mobile.persistence.model.Species_Table;
import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpeciesSelectionFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private Unbinder unbinder;

    @BindView(R.id.species_selection_fragment_spinner)
    Spinner speciesClassSpinner;

    @BindView(R.id.species_selection_fragment_autocomplete)
    AutoCompleteTextView speciesAutoCompleteTextView;

    private Context context;
    private SpeciesClassAdapter speciesClassAdapter;
    private FilterableSpeciesAdapter filterableSpeciesAdapter;
    private SpeciesStub.Class selectedClass;
    private InputValidator<SpeciesSelection> speciesSelectionInputValidator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_species_selection, container, false);
        unbinder = ButterKnife.bind(this, view);

        speciesClassAdapter = new SpeciesClassAdapter(context, android.R.layout.simple_spinner_item);
        speciesClassSpinner.setAdapter(speciesClassAdapter);
        speciesClassSpinner.setOnItemSelectedListener(this);
        filterableSpeciesAdapter = new FilterableSpeciesAdapter(context, speciesClassAdapter.getItem(0));
        speciesAutoCompleteTextView.setAdapter(filterableSpeciesAdapter);
        speciesAutoCompleteTextView.setOnItemClickListener(this);
        speciesSelectionInputValidator = InputValidators.getSpeciesSelectionValidator(context);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    // species class selection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedClass = (SpeciesStub.Class) parent.getItemAtPosition(position);
        if (!selectedClass.equals(filterableSpeciesAdapter.getSpeciesClass())) {
            speciesAutoCompleteTextView.setText("");
        }
        filterableSpeciesAdapter.setSpeciesClass(selectedClass);
        filterableSpeciesAdapter.getFilter().filter(speciesAutoCompleteTextView.getText(), speciesAutoCompleteTextView);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // species selection
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Species clicked = (Species) parent.getItemAtPosition(position);
        speciesAutoCompleteTextView.setText(clicked.getName());
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public Optional<Species> getValidSpecies() {
        SpeciesSelection currentSelection = new SpeciesSelection(selectedClass, speciesAutoCompleteTextView.getText().toString());
        Optional<Species> result = Optional.absent();
        Optional<ValidationError> validationError = speciesSelectionInputValidator.validate(currentSelection);
        if (validationError.isPresent()) {
            speciesAutoCompleteTextView.setError(validationError.get().getErrorMessage());
            speciesAutoCompleteTextView.requestFocus();
        } else {
            result = Optional.fromNullable(SQLite.select()
                    .from(Species.class)
                    .where(Species_Table.speciesClass.eq(currentSelection.getSpeciesClass()))
                    .and(Species_Table.name.eq(currentSelection.getSpeciesName()))
                    .querySingle());
        }
        return result;
    }

    public void setSpeciesClass(SpeciesStub.Class speciesClass) {
        selectedClass = speciesClass;
        filterableSpeciesAdapter.setSpeciesClass(selectedClass);
        speciesClassSpinner.setSelection(speciesClassAdapter.getPosition(speciesClass));
    }

    public void setSpeciesName(String speciesName) {
        speciesAutoCompleteTextView.setText(speciesName);
    }
}
