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

package com.wlobs.wilqor.mobile.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.persistence.model.Species;
import com.wlobs.wilqor.mobile.persistence.model.Species_Table;
import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wilqor
 */
public class FilterableSpeciesAdapter extends BaseAdapter implements Filterable {
    private static final int MAX_RESULTS = 20;

    private Context context;
    private List<Species> speciesList = new ArrayList<>();
    private SpeciesStub.Class speciesClass;

    public FilterableSpeciesAdapter(Context context, SpeciesStub.Class speciesClass) {
        this.context = context;
        this.speciesClass = speciesClass;
    }

    @Override
    public int getCount() {
        return speciesList.size();
    }

    @Override
    public Species getItem(int position) {
        return speciesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.species_list_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.species_list_item_name);
        name.setText(getItem(position).getName());
        return convertView;
    }

    public void setSpeciesClass(SpeciesStub.Class speciesClass) {
        this.speciesClass = speciesClass;
    }

    public SpeciesStub.Class getSpeciesClass() {
        return speciesClass;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Species> filteredSpecies = SQLite.select()
                            .from(Species.class)
                            .where(Species_Table.name.like("%" + constraint + "%"))
                            .and(Species_Table.speciesClass.eq(speciesClass))
                            .orderBy(Species_Table.name, true)
                            .limit(MAX_RESULTS)
                            .queryList();
                    filterResults.values = filteredSpecies;
                    filterResults.count = filteredSpecies.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint != null) {
                    speciesList = (List<Species>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }
}
