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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fernandocejas.arrow.optional.Optional;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.adapters.DividerItemDecoration;
import com.wlobs.wilqor.mobile.activity.adapters.ObservationsAdapter;
import com.wlobs.wilqor.mobile.activity.adapters.OnItemClickListener;
import com.wlobs.wilqor.mobile.activity.formatting.DateFormatters;
import com.wlobs.wilqor.mobile.activity.formatting.SpeciesFormatters;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtilities;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.persistence.model.Observation;
import com.wlobs.wilqor.mobile.persistence.model.Observation_Table;
import com.wlobs.wilqor.mobile.persistence.sync.SyncUtilities;
import com.wlobs.wilqor.mobile.persistence.sync.SyncUtility;
import com.wlobs.wilqor.mobile.rest.api.ObservationsService;
import com.wlobs.wilqor.mobile.rest.api.RestServices;
import com.wlobs.wilqor.mobile.rest.api.SpeciesService;
import com.wlobs.wilqor.mobile.rest.api.VotesService;
import com.wlobs.wilqor.mobile.rest.task.RemoteTask;
import com.wlobs.wilqor.mobile.rest.task.RemoteTasks;
import com.wlobs.wilqor.mobile.rest.task.exceptions.ErrorResponseException;
import com.wlobs.wilqor.mobile.rest.task.exceptions.UnauthorizedException;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ObservationsActivity extends NavigationActivity implements OnItemClickListener {
    @BindView(R.id.observations_last_sync_date)
    TextView lastSyncDate;

    @BindView(R.id.observations_sync_button)
    ImageButton syncButton;

    @BindView(R.id.drawer_layout)
    View topLevelLayout;

    @BindView(R.id.observations_recycler_view)
    RecyclerView recyclerView;

    private SyncUtility syncUtility;
    private AuthUtility authUtility;
    private SpeciesService speciesService;
    private ObservationsService observationsService;
    private VotesService votesService;
    private Optional<SynchronizationTask> currentSyncTask;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private FlowCursorList<Observation> observationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_observations, contentFrameLayout);
        ButterKnife.bind(this);
        syncUtility = SyncUtilities.getSyncUtility(getApplicationContext());
        authUtility = AuthUtilities.getAuthUtility(getApplicationContext());
        speciesService = RestServices.getSpeciesService(getApplicationContext(), authUtility, getResources());
        observationsService = RestServices.getObservationsService(getApplicationContext(), authUtility, getResources());
        votesService = RestServices.getVotesService(getApplicationContext(), authUtility, getResources());

        layoutManager = new LinearLayoutManager(this);
        observationsList = SQLite.select()
                .from(Observation.class)
                .where(Observation_Table.author.eq(authUtility.getLogin().get()))
                .and(Observation_Table.deleted.eq(false))
                .orderBy(Observation_Table.dateUtcTimestamp, false)
                .cursorList();
        adapter = new ObservationsAdapter(observationsList,
                this,
                DateFormatters.getFullDateFormatter(),
                SpeciesFormatters.getSpeciesFormatter());
        // set if all item views are of the same height and width for performance
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.observations_fab)
    public void onFabClick() {
        startActivity(new Intent(this, ObservationCreationActivity.class));
    }

    @OnClick(R.id.observations_sync_button)
    public void onSyncButtonClick() {
        performSynchronization();
    }

    @Override
    protected void onResume() {
        super.onResume();

        currentSyncTask = Optional.absent();
        updateLastSyncDateLabel();
        refreshObservationsList();
        if (syncUtility.shouldSync()) {
            performSynchronization();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        observationsList.close();
    }

    @Override
    public void onBackPressed() {
        // since this is the 'home' activity, going back is not allowed
        // user may not go back to not logged in state else than via navigation panel
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (currentSyncTask.isPresent()) {
            currentSyncTask.get().cancel(true);
        }
    }

    @Override
    public void onItemClick(int position) {
        Observation clicked = observationsList.getItem(position);
        Intent intent = new Intent(this, ObservationDetailsActivity.class);
        intent.putExtra(ObservationDetailsActivity.INTENT_OBSERVATION_KEY, clicked);
        startActivity(intent);
    }

    private void updateLastSyncDateLabel() {
        lastSyncDate.setText(syncUtility.getLastSyncDateString());
    }

    private void performSynchronization() {
        SynchronizationTask syncTask = new SynchronizationTask(
                syncUtility,
                authUtility,
                RemoteTasks.getSyncTasks(
                        speciesService,
                        observationsService,
                        votesService,
                        authUtility.getLogin().get()));
        currentSyncTask = Optional.of(syncTask);
        syncTask.execute();
    }

    private void logTheUserOut() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private Snackbar getSyncErrorSnackbar() {
        Snackbar snackbar = Snackbar.make(topLevelLayout, getString(R.string.error_sync_failed), Snackbar.LENGTH_LONG);
        snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSynchronization();
            }
        });
        return snackbar;
    }

    private ProgressDialog getSyncProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.observations_sync_progress_message));
        dialog.setCancelable(false);
        return dialog;
    }

    private enum SynchronizationResult {
        SUCCESS,
        FAILURE,
        UNAUTHORIZED,
        CANCELLED
    }

    private void refreshObservationsList() {
        observationsList.refresh();
        adapter.notifyDataSetChanged();
    }

    private class SynchronizationTask extends AsyncTask<Void, Void, SynchronizationResult> {
        private final SyncUtility syncUtility;
        private final AuthUtility authUtility;
        private final List<RemoteTask> remoteTasks;
        private final ProgressDialog progressDialog;

        private SynchronizationTask(SyncUtility syncUtility, AuthUtility authUtility, List<RemoteTask> remoteTasks) {
            this.syncUtility = syncUtility;
            this.authUtility = authUtility;
            this.remoteTasks = remoteTasks;
            this.progressDialog = ObservationsActivity.this.getSyncProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected SynchronizationResult doInBackground(Void... params) {
            SynchronizationResult result = SynchronizationResult.SUCCESS;
            try {
                for (RemoteTask t : remoteTasks) {
                    if (isCancelled()) {
                        result = SynchronizationResult.CANCELLED;
                        break;
                    }
                    t.perform();
                }
            } catch (IOException | ErrorResponseException e) {
                result = SynchronizationResult.FAILURE;
            } catch (UnauthorizedException e) {
                result = SynchronizationResult.UNAUTHORIZED;
            }
            return result;
        }

        @Override
        protected void onCancelled() {
            progressDialog.hide();
        }

        @Override
        protected void onPostExecute(SynchronizationResult synchronizationResult) {
            progressDialog.hide();
            switch (synchronizationResult) {
                case SUCCESS:
                    syncUtility.updateLastSyncTimestampToNow();
                    ObservationsActivity.this.updateLastSyncDateLabel();
                    ObservationsActivity.this.refreshObservationsList();
                    break;
                case FAILURE:
                    ObservationsActivity.this.getSyncErrorSnackbar().show();
                    break;
                case UNAUTHORIZED:
                    authUtility.clearAllTokens();
                    ObservationsActivity.this.logTheUserOut();
                    break;
            }
        }
    }
}
