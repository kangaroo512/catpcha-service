package com.languageproject.v1.services;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class JwtService implements SignUpTokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long expirationMs = 9000000;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public Mono<String> generateToken(String email) {

        return Mono.fromCallable(() -> {

            return Jwts.builder()
                .setSubject(email)
                .setIssuer("language-app")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey())
                .compact();

        }).subscribeOn(Schedulers.boundedElastic());


    }

    @Override
    public Mono<Boolean> IsTokenValid(String token) {

        return Mono.fromCallable(() -> {

            try {
            Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch(Exception e) {
            return false;
        } 

        }).subscribeOn(Schedulers.boundedElastic());






        
    }
    

}
