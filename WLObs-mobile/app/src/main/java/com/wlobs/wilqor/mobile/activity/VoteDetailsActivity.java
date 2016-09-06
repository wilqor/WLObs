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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatter;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatters;
import com.wlobs.wilqor.mobile.persistence.model.Vote;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoteDetailsActivity extends NavigationActivity {
    public static final String INTENT_VOTE_KEY = "VOTE_IN_INTENT";

    @BindView(R.id.vote_details_date)
    TextView dateVoted;

    @BindView(R.id.vote_details_author)
    TextView observationAuthor;

    @BindView(R.id.vote_details_species_class)
    TextView speciesClass;

    @BindView(R.id.vote_details_species_description)
    TextView speciesDescription;

    @BindView(R.id.vote_details_delete_button)
    Button deleteButton;

    private DateFormatter dateFormatter;
    private Vote currentVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_vote_details, contentFrameLayout);
        ButterKnife.bind(this);
        dateFormatter = DateFormatters.getFullDateFormatter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        currentVote = (Vote) intent.getSerializableExtra(INTENT_VOTE_KEY);
        if (currentVote != null) {
            dateVoted.setText(dateFormatter.format(currentVote.getDateUtcTimestamp()));
            observationAuthor.setText(currentVote.getObservationOwner());
            speciesClass.setText(currentVote.getSpecies().getSpeciesClass().asString());
            speciesDescription.setText(currentVote.getSpecies().getName());
            deleteButton.setClickable(true);
        } else {
            finish();
        }
    }

    @OnClick(R.id.vote_details_delete_button)
    public void onClickVoteDeleteButton() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.vote_details_delete_confirmation)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentVote.setDeleted(true);
                        currentVote.update();
                        VoteDetailsActivity.this.finish();
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
}
