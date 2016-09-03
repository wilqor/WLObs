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
import android.support.v7.app.AppCompatActivity;

import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtilities;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;

/**
 * This activity plays the role of application entry point and does not present
 * a concrete view. Its role is to redirect user to the default logged in activity
 * or to the log in activity, depending on the authentication state.
 */
public class MainActivity extends AppCompatActivity {
    private AuthUtility authUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        authUtility = AuthUtilities.getAuthUtility(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!authUtility.isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, ObservationsActivity.class));
        }
    }
}
