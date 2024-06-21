package com.jwt.rolebasedjwt.service;

import com.jwt.rolebasedjwt.dto.JwtAuthenticationResponse;
import com.jwt.rolebasedjwt.dto.RefreshTokenRequest;
import com.jwt.rolebasedjwt.dto.SignInRequest;
import com.jwt.rolebasedjwt.dto.SignUpRequest;
import com.jwt.rolebasedjwt.entities.User;

public interface AuthenticationService {
    User signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signin(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
