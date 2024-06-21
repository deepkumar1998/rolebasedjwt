package com.jwt.rolebasedjwt.service.Impl;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.jwt.rolebasedjwt.entities.User;
import com.jwt.rolebasedjwt.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImplementation implements JwtService{
    private final static String secretKey="ahfgfiuagfgaiugit8763rfhga8f2ehjhd761239xaagufahgsruggrhiu4868q3bfgw8e7ftgf27t3grtrgdftasf78dvag8atewhebdiguq584vug65tasu54jvuta75wuvuyg656afhgag";
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
        .signWith(getSignKey(),SignatureAlgorithm.HS512)
        .compact();

    }

    private <T> T extractClaim(String token, Function<Claims, T>claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
       return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }




    private Key getSignKey() {
       byte[] key=Decoders.BASE64.decode(secretKey);
       return Keys.hmacShaKeyFor(key);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
       return extractClaim(token, Claims::getExpiration).before(new Date());
    }

   @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+1000*60*24*7))
        .signWith(getSignKey(),SignatureAlgorithm.HS512)
        .compact();
       
    }
}
