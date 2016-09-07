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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.adapters.DividerItemDecoration;
import com.wlobs.wilqor.mobile.activity.adapters.OnItemClickListener;
import com.wlobs.wilqor.mobile.activity.adapters.VotesAdapter;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatters;
import com.wlobs.wilqor.mobile.activity.formatting.SpeciesFormatters;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtilities;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.persistence.model.Vote;
import com.wlobs.wilqor.mobile.persistence.model.Vote_Table;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VotesActivity extends NavigationActivity implements OnItemClickListener {
    @BindView(R.id.votes_recycler_view)
    RecyclerView recyclerView;

    private AuthUtility authUtility;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FlowCursorList<Vote> votesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_votes, contentFrameLayout);
        ButterKnife.bind(this);
        authUtility = AuthUtilities.getAuthUtility(getApplicationContext());
        layoutManager = new LinearLayoutManager(this);
        votesList = SQLite.select()
                .from(Vote.class)
                .where(Vote_Table.voter.eq(authUtility.getLogin().get()))
                .and(Vote_Table.deleted.eq(false))
                .orderBy(Vote_Table.dateUtcTimestamp, false)
                .cursorList();
        adapter = new VotesAdapter(votesList,
                this,
                DateFormatters.getFullDateFormatter(),
                SpeciesFormatters.getSpeciesFormatter());
        // set if all item views are of the same height and width for performance
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshVotesList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        votesList.close();
    }

    @Override
    public void onItemClick(int position) {
        Vote clickedVote = votesList.getItem(position);
        Intent intent = new Intent(this, VoteDetailsActivity.class);
        intent.putExtra(VoteDetailsActivity.INTENT_VOTE_KEY, clickedVote);
        startActivity(intent);
    }

    private void refreshVotesList() {
        votesList.refresh();
        adapter.notifyDataSetChanged();
    }
}
