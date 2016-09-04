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

package com.wlobs.wilqor.mobile.persistence.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wlobs.wilqor.mobile.R;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author wilqor
 */
final class SyncUtilityImpl implements SyncUtility {
    private static final String LAST_SYNC_TIMESTAMP_KEY = "LAST_SYNC_TIMESTAMP";
    /**
     * Allow up to 30 minutes between synchronizations.
     */
    private static final long MAX_TIME_BETWEEN_SYNCS_IN_MILLISECONDS = TimeUnit.MINUTES.toMillis(30);
    /**
     * Default timestamp set to 0 ensures that resetting stored timestamp will always prompt
     * to perform a sync by any following invocations of {@link #shouldSync()}.
     */
    private static final long DEFAULT_SYNC_TIMESTAMP = 0;

    private final SharedPreferences preferences;
    private final Context context;
    private final SyncDateFormatter dateFormatter;

    SyncUtilityImpl(Context context, SyncDateFormatter dateFormatter) {
        this.context = context;
        this.dateFormatter = dateFormatter;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void updateLastSyncTimestampToNow() {
        updateLastSyncTimestamp(new Date().getTime());
    }

    @Override
    public boolean shouldSync() {
        long timestampOfNow = new Date().getTime();
        long storedTimestamp = getStoredTimestamp();
        return timestampOfNow - storedTimestamp > MAX_TIME_BETWEEN_SYNCS_IN_MILLISECONDS;
    }

    @Override
    public void resetLastSyncTimestamp() {
        updateLastSyncTimestamp(DEFAULT_SYNC_TIMESTAMP);
    }

    @Override
    public String getLastSyncDateString() {
        String dateString = context.getString(R.string.observations_last_sync_never);
        long storedTimestamp = getStoredTimestamp();
        if (storedTimestamp != DEFAULT_SYNC_TIMESTAMP) {
            dateString = dateFormatter.formatSyncDate(new Date(storedTimestamp));
        }
        return dateString;
    }

    private void updateLastSyncTimestamp(long timestamp) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LAST_SYNC_TIMESTAMP_KEY, timestamp);
        editor.apply();
    }

    private long getStoredTimestamp() {
        return preferences.getLong(LAST_SYNC_TIMESTAMP_KEY, DEFAULT_SYNC_TIMESTAMP);
    }
}
