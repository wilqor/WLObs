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
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wlobs.wilqor.mobile.rest.model.SpeciesStub;

/**
 * @author wilqor
 */
public class SpeciesClassAdapter extends ArrayAdapter<SpeciesStub.Class> {

    public SpeciesClassAdapter(Context context, int resource) {
        super(context, resource, new SpeciesStub.Class[]{
                SpeciesStub.Class.AMPHIBIAN,
                SpeciesStub.Class.REPTILE,
                SpeciesStub.Class.FISH,
                SpeciesStub.Class.BIRD,
                SpeciesStub.Class.MAMMAL
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text = (TextView) convertView;
        if (text == null) {
            text = (TextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        text.setText(getItem(position).asString());
        return text;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView text = (TextView) convertView;
        if (text == null) {
            text = (TextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        text.setText(getItem(position).asString());
        return text;
    }
}
