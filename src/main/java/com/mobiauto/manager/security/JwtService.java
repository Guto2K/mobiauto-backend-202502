package com.mobiauto.manager.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
	
	@Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private long expirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
    	
    	if (secretKey == null || secretKey.length() < 32) {
            throw new IllegalStateException("A chave JWT deve ter pelo menos 32 caracteres");
        }
        // Converte a chave secreta de String para SecretKey
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expirationMs);

        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(key)
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        Jws<Claims> jws = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token);
        return jws.getPayload();
    }
}
