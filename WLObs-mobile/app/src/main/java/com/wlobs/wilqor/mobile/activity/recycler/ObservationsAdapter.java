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

package com.wlobs.wilqor.mobile.activity.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatter;
import com.wlobs.wilqor.mobile.activity.formatting.SpeciesFormatter;
import com.wlobs.wilqor.mobile.persistence.model.Observation;
import com.wlobs.wilqor.mobile.persistence.model.Species;

/**
 * @author wilqor
 */
public final class ObservationsAdapter extends RecyclerView.Adapter<ObservationsAdapter.ViewHolder> {
    private final FlowCursorList<Observation> observationsList;
    private final OnItemClickListener clickListener;
    private final DateFormatter dateFormatter;
    private final SpeciesFormatter speciesFormatter;

    public ObservationsAdapter(FlowCursorList<Observation> observationsList,
                               OnItemClickListener clickListener,
                               DateFormatter dateFormatter,
                               SpeciesFormatter speciesFormatter) {
        this.observationsList = observationsList;
        this.clickListener = clickListener;
        this.dateFormatter = dateFormatter;
        this.speciesFormatter = speciesFormatter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.observation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Observation observation = observationsList.getItem(position);
        Species observedSpecies = observation.getSpecies();
        holder.setSpeciesClassLabelText(observedSpecies.getSpeciesClass().asString());
        holder.setDateLabelText(dateFormatter.format(observation.getDateUtcTimestamp()));
        holder.setSpeciesDescriptionLabelText(speciesFormatter.format(observedSpecies));
        holder.setVotesCountLabelText(String.valueOf(observation.getVotesCount()));
        holder.setRestrictionIconVisibility(observation.isRestricted());
    }

    @Override
    public int getItemCount() {
        return observationsList.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView speciesClassLabel;
        private TextView dateLabel;
        private TextView speciesDescriptionLabel;
        private TextView votesCountLabel;
        private ImageView restrictionIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            speciesClassLabel = (TextView) itemView.findViewById(R.id.observation_item_species_class);
            dateLabel = (TextView) itemView.findViewById(R.id.observation_item_date);
            speciesDescriptionLabel = (TextView) itemView.findViewById(R.id.observation_item_species_description);
            votesCountLabel = (TextView) itemView.findViewById(R.id.observation_item_votes_count);
            restrictionIcon = (ImageView) itemView.findViewById(R.id.observation_item_restriction_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getLayoutPosition());
                }
            });
        }

        public void setSpeciesClassLabelText(String text) {
            speciesClassLabel.setText(text);
        }

        public void setDateLabelText(String text) {
            dateLabel.setText(text);
        }

        public void setSpeciesDescriptionLabelText(String text) {
            speciesDescriptionLabel.setText(text);
        }

        public void setVotesCountLabelText(String text) {
            votesCountLabel.setText(text);
        }

        public void setRestrictionIconVisibility(boolean visible) {
            restrictionIcon.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
