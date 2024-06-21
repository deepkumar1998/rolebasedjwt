package com.jwt.rolebasedjwt.service;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService  {
    UserDetailsService userDetailsService();
}
