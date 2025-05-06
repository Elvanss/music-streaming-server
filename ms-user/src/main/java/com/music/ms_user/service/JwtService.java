package com.music.ms_user.service;

import com.music.ms_user.security.UserDetailsImpl;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Slf4j
@Component
public class JwtService {

   private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    private final KeyPair keyPair;

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expiration}")
    private long expiration;

    public JwtService() {
        this.keyPair = generateKeyPair();
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public String generateToken(UserDetailsImpl userDetails) {
        @SuppressWarnings("deprecation")
        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .expiration(new Date(System.currentTimeMillis() + expiration));

        /*  Add custom claims
        if (userDetails.getSemesterId() != null) {
           builder.claim("semesterId", userDetails.getSemesterId());
        }
        */
        return jwtBuilder.compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            LOGGER.error("JWT token is null or empty");
            return false;
        }

        try {
            Jwts.parser()
                .verifyWith(getPublicKey())
                .build()
                .parseSignedClaims(token);

            return true;
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            LOGGER.error("JWT token is invalid: {}", e.getMessage());
        }
        return false;
    }

    public String getEmailFromJwtToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }
}