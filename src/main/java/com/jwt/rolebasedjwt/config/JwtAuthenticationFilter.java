package com.jwt.rolebasedjwt.config;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.rolebasedjwt.service.JwtService;
import com.jwt.rolebasedjwt.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       
                final String authHeader=request.getHeader("Authorization");

                final String jwt;

                final String userEmail;

                if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")){
                    filterChain.doFilter(request, response);
                    return;

                }
                jwt=authHeader.substring(7);
                userEmail=jwtService.extractUserName(jwt);

                if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication()==null) {
                    UserDetails userDetails=userService.userDetailsService().loadUserByUsername(userEmail);
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        SecurityContext SecurityContext=SecurityContextHolder.createEmptyContext();

                        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContext.setAuthentication(token);
                        SecurityContextHolder.setContext(SecurityContext);
                    }
                }

                filterChain.doFilter(request, response);

        }
    
}
