package com.jwt.rolebasedjwt.service.Impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.rolebasedjwt.dto.JwtAuthenticationResponse;
import com.jwt.rolebasedjwt.dto.RefreshTokenRequest;
import com.jwt.rolebasedjwt.dto.SignInRequest;
import com.jwt.rolebasedjwt.dto.SignUpRequest;
import com.jwt.rolebasedjwt.entities.Role;
import com.jwt.rolebasedjwt.entities.User;
import com.jwt.rolebasedjwt.repository.UserRepository;
import com.jwt.rolebasedjwt.service.AuthenticationService;
import com.jwt.rolebasedjwt.service.JwtService;





@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public User signUp(SignUpRequest signUpRequest){
        User user =new User();

        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
        
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest signInRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
         signInRequest.getPassword()));

         var user= userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));
         var jwt=jwtService.generateToken(user);
         var refreshToken=jwtService.generateRefreshToken(new HashMap<>(),user);

         JwtAuthenticationResponse jwtAuthenticationResponse=new JwtAuthenticationResponse();
         jwtAuthenticationResponse.setToken(jwt);
         jwtAuthenticationResponse.setRefreshToken(refreshToken);

         return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail=jwtService.extractUserName(refreshTokenRequest.getToken());

        User user=userRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt=jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse=new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
   
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
