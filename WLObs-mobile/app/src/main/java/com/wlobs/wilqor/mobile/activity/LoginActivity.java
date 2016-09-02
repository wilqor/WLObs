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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.fernandocejas.arrow.optional.Optional;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.validation.InputValidator;
import com.wlobs.wilqor.mobile.activity.validation.InputValidators;
import com.wlobs.wilqor.mobile.activity.validation.ValidationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A login screen that offers login via login/password.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    @BindView(R.id.login)
    EditText loginView;

    @BindView(R.id.password)
    EditText passwordView;

    @BindView(R.id.login_progress)
    View progressView;

    @BindView(R.id.login_container)
    View loginFormView;

    @BindView(R.id.login_wrapper)
    View loginWrapperView;

    @BindView(R.id.registration_wrapper)
    View registrationWrapperView;

    private InputValidator<String> loginValidator;
    private InputValidator<String> passwordValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        loginValidator = InputValidators.getLoginValidator(getApplicationContext());
        passwordValidator = InputValidators.getPasswordValidator(getApplicationContext());
    }

    @OnClick(R.id.register_prompt)
    public void onRegisterPromptClick() {
        loginWrapperView.setVisibility(View.GONE);
        registrationWrapperView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.login_prompt)
    public void onLoginPromptClick() {
        registrationWrapperView.setVisibility(View.GONE);
        loginWrapperView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClick() {
        if (isInputValid()) {
            showProgress(true);
            // TODO define a login task
            mAuthTask = new UserLoginTask(loginView.getText().toString(), passwordView.getText().toString());
            mAuthTask.execute((Void) null);
        }
    }

    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {
        if (isInputValid()) {
            showProgress(true);
            // TODO define a register task
            mAuthTask = new UserLoginTask(loginView.getText().toString(), passwordView.getText().toString());
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public void onBackPressed() {
        // disable going to other activities
        moveTaskToBack(true);
    }

    private boolean isInputValid() {
        loginView.setError(null);
        passwordView.setError(null);
        String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        Optional<EditText> focusView = Optional.absent();
        Optional<ValidationError> loginError = loginValidator.validate(login);
        Optional<ValidationError> passwordError = passwordValidator.validate(password);
        if (loginError.isPresent()) {
            focusView = Optional.of(loginView);
            loginView.setError(loginError.get().getErrorMessage());
        }
        if (passwordError.isPresent()) {
            if (!focusView.isPresent()) {
                focusView = Optional.of(passwordView);
            }
            passwordView.setError(passwordError.get().getErrorMessage());
        }
        if (focusView.isPresent()) {
            focusView.get().requestFocus();
        }
        return !loginError.isPresent() && !passwordError.isPresent();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String login;
        private final String password;

        UserLoginTask(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO actually perform login
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                finish();
            } else {
                passwordView.setError(getString(R.string.error_incorrect_password));
                passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

