package com.example.restapiwithtoken.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claim) {
        final Claims claims = extractAllClaims(token);
        return claim.apply(claims);
    }

    public String generateToken(UserDetails userdetails) {
        return generatetoken(new HashMap<>(), userdetails);
    }

    private String generatetoken(Map<String, Object> claims, UserDetails userdetails) {
        return Jwts.builder()
        .setClaims(claims)
        .setSubject(userdetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
    } 

    public boolean validateToken(String token, UserDetails userdetails) {
        final String username = extractUsername(token);
        return (username.equals(userdetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
       return extractClaim(token, Claims::getExpiration);           
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }


    private Key getSignKey() {

        byte[] keybytes = Decoders.BASE64.decode("8c5665decd59abe802fc6483e4afeb4c7e53d25357e514a97f8b3ae773e24d30");
        return Keys.hmacShaKeyFor(keybytes);
    }

}
