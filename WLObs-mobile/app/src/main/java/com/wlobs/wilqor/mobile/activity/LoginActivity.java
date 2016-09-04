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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fernandocejas.arrow.optional.Optional;
import com.wlobs.wilqor.mobile.R;
import com.wlobs.wilqor.mobile.activity.validation.InputValidator;
import com.wlobs.wilqor.mobile.activity.validation.InputValidators;
import com.wlobs.wilqor.mobile.activity.validation.ValidationError;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtilities;
import com.wlobs.wilqor.mobile.persistence.auth.AuthUtility;
import com.wlobs.wilqor.mobile.persistence.sync.SyncUtilities;
import com.wlobs.wilqor.mobile.persistence.sync.SyncUtility;
import com.wlobs.wilqor.mobile.rest.api.RestServices;
import com.wlobs.wilqor.mobile.rest.api.UsersService;
import com.wlobs.wilqor.mobile.rest.model.AuthAndRefreshTokensDto;
import com.wlobs.wilqor.mobile.rest.model.CredentialsDto;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A login screen that offers login via login/password.
 */
public class LoginActivity extends AppCompatActivity {
    private enum Operation {
        LOGIN,
        REGISTRATION
    }

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

    @BindView(R.id.topLoginLayout)
    View topLayout;

    @BindView(R.id.operation_error)
    TextView operationError;

    private InputValidator<String> loginValidator;
    private InputValidator<String> passwordValidator;
    private UsersService usersService;
    private AuthUtility authUtility;
    private SyncUtility syncUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        loginValidator = InputValidators.getLoginValidator(getApplicationContext());
        passwordValidator = InputValidators.getPasswordValidator(getApplicationContext());
        usersService = RestServices.getUsersService(getApplicationContext());
        authUtility = AuthUtilities.getAuthUtility(getApplicationContext());
        syncUtility = SyncUtilities.getSyncUtility(getApplicationContext());
    }

    @OnClick(R.id.register_prompt)
    public void onRegisterPromptClick() {
        operationError.setText("");
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
            attemptLogin();
        }
    }

    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {
        if (isInputValid()) {
            attemptRegistration();
        }
    }

    @Override
    public void onBackPressed() {
        // disable going to other activities
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        resetErrorInfo();
    }

    private void attemptLogin() {
        showProgress(true);
        final String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        Call<AuthAndRefreshTokensDto> call = usersService.login(
                new CredentialsDto(login, password));
        call.enqueue(new Callback<AuthAndRefreshTokensDto>() {
            @Override
            public void onResponse(Call<AuthAndRefreshTokensDto> call, Response<AuthAndRefreshTokensDto> response) {
                if (response.isSuccessful()) {
                    AuthAndRefreshTokensDto tokensDto = response.body();
                    authUtility.saveTokens(tokensDto.getAuthToken(), tokensDto.getRefreshToken(), login);
                    syncUtility.resetLastSyncTimestamp();
                    showProgress(false);
                    goToMainActivity();
                } else {
                    operationError.setText(getText(R.string.error_invalid_credentials));
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<AuthAndRefreshTokensDto> call, Throwable t) {
                showNetworkErrorSnackbar(Operation.LOGIN);
                showProgress(false);
            }
        });
    }

    private void attemptRegistration() {
        showProgress(true);
        final String login = loginView.getText().toString();
        String password = passwordView.getText().toString();
        Call<AuthAndRefreshTokensDto> call = usersService.register(
                new CredentialsDto(
                        login,
                        password));
        call.enqueue(new Callback<AuthAndRefreshTokensDto>() {
            @Override
            public void onResponse(Call<AuthAndRefreshTokensDto> call, Response<AuthAndRefreshTokensDto> response) {
                if (response.isSuccessful()) {
                    AuthAndRefreshTokensDto tokensDto = response.body();
                    authUtility.saveTokens(tokensDto.getAuthToken(), tokensDto.getRefreshToken(), login);
                    syncUtility.resetLastSyncTimestamp();
                    showProgress(false);
                    goToMainActivity();
                } else {
                    operationError.setText(getText(R.string.error_login_already_taken));
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<AuthAndRefreshTokensDto> call, Throwable t) {
                showNetworkErrorSnackbar(Operation.REGISTRATION);
                showProgress(false);
            }
        });
    }

    private boolean isInputValid() {
        resetErrorInfo();
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

    private void resetErrorInfo() {
        operationError.setText("");
        loginView.setError(null);
        passwordView.setError(null);
    }

    private void showProgress(final boolean show) {
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showNetworkErrorSnackbar(final Operation op) {
        Snackbar snackbar = Snackbar.make(topLayout, getString(R.string.network_error), Snackbar.LENGTH_LONG);
        if (Operation.LOGIN == op) {
            snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoginButtonClick();
                }
            });
        } else {
            snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRegisterButtonClick();
                }
            });
        }
        snackbar.show();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

