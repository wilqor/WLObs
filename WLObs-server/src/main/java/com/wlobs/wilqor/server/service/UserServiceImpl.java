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

package com.wlobs.wilqor.server.service;

import com.wlobs.wilqor.server.persistence.model.User;
import com.wlobs.wilqor.server.persistence.repository.UserRepository;
import com.wlobs.wilqor.server.rest.model.*;
import com.wlobs.wilqor.server.service.exceptions.InvalidPasswordException;
import com.wlobs.wilqor.server.service.exceptions.InvalidRefreshTokenException;
import com.wlobs.wilqor.server.service.exceptions.LoginAlreadyTakenException;
import com.wlobs.wilqor.server.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author wilqor
 */
@Service
public final class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthAndRefreshTokensDto register(CredentialsDto credentialsDto) {
        Optional<User> existing = userRepository.findByLogin(credentialsDto.getLogin());
        existing.ifPresent(u -> {
            throw new LoginAlreadyTakenException(credentialsDto.getLogin());
        });
        String refreshToken = tokenService.generateRefreshToken();
        User user = new User.Builder()
                .login(credentialsDto.getLogin())
                .passwordHash(passwordEncoder.encode(credentialsDto.getPassword()))
                .refreshTokens(Collections.singletonList(refreshToken))
                .roles(Collections.singletonList(User.Role.ROLE_USER))
                .build();
        userRepository.save(user);
        String authToken = tokenService.buildAuthToken(user.getLogin(), user.getRoles());
        return new AuthAndRefreshTokensDto(
                authToken,
                refreshToken
        );
    }

    @Override
    public AuthAndRefreshTokensDto resetPassword(String login, ResetPasswordDto resetPasswordDto) {
        User user = findUserWithMatchingPasswordOrThrow(login, resetPasswordDto.getOldPassword());
        String newAuthToken = tokenService.buildAuthToken(user.getLogin(), user.getRoles());
        String newRefreshToken = tokenService.generateRefreshToken();
        user.setPasswordHash(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        user.setRefreshTokens(Collections.singletonList(newRefreshToken));
        userRepository.save(user);
        return new AuthAndRefreshTokensDto(
                newAuthToken,
                newRefreshToken
        );
    }

    @Override
    public AuthAndRefreshTokensDto authorize(CredentialsDto credentialsDto) {
        User user = findUserWithMatchingPasswordOrThrow(credentialsDto.getLogin(), credentialsDto.getPassword());
        String newAuthToken = tokenService.buildAuthToken(user.getLogin(), user.getRoles());
        String newRefreshToken = tokenService.generateRefreshToken();
        user.setRefreshTokens(addTokens(user.getRefreshTokens(), newRefreshToken));
        userRepository.save(user);
        return new AuthAndRefreshTokensDto(
                newAuthToken,
                newRefreshToken
        );
    }

    @Override
    public AuthTokenDto refreshToken(LoginAndRefreshTokenDto loginAndRefreshTokenDto) {
        User user = findUserOrThrow(loginAndRefreshTokenDto.getLogin());
        if (!user.getRefreshTokens().contains(loginAndRefreshTokenDto.getRefreshToken())) {
            throw new InvalidRefreshTokenException(loginAndRefreshTokenDto.getLogin(), loginAndRefreshTokenDto.getRefreshToken());
        }
        return new AuthTokenDto(tokenService.buildAuthToken(user.getLogin(), user.getRoles()));
    }

    private User findUserWithMatchingPasswordOrThrow(String login, String password) {
        User user = findUserOrThrow(login);
        if (!passwordMatching(user.getPasswordHash(), password)) {
            throw new InvalidPasswordException();
        }
        return user;
    }

    private User findUserOrThrow(String login) {
        Optional<User> requested = userRepository.findByLogin(login);
        return requested.orElseThrow(() -> new UserNotFoundException(login));
    }

    private boolean passwordMatching(String hashPassword, String rawPassword) {
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

    private List<String> addTokens(List<String> existingTokens, String newToken) {
        List<String> result = new ArrayList<>(existingTokens);
        result.add(newToken);
        return result;
    }
}