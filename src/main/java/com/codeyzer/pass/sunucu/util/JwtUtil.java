package com.codeyzer.pass.sunucu.util;

import com.codeyzer.pass.sunucu.config.CodeyzerPassConfigurationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final CodeyzerPassConfigurationProperties codeyzerPassConfigurationProperties;

    public String generateAccessToken(String kullaniciKimlik) {
        return generateToken(kullaniciKimlik, "access", new Date(System.currentTimeMillis() + codeyzerPassConfigurationProperties.getAccessTokenExpiration()));
    }

    public String generateRefreshToken(String kullaniciKimlik) {
        return generateToken(kullaniciKimlik, "refresh", new Date(System.currentTimeMillis() + codeyzerPassConfigurationProperties.getRefreshTokenExpiration()));
    }

    private String generateToken(String sub, String aud, Date expiration) {
        return Jwts.builder()
                .issuer(codeyzerPassConfigurationProperties.getJwtIssuer())
                .subject(sub)
                .audience()
                    .add(aud)
                    .and()
                .issuedAt(new Date())
                .expiration(expiration)
                .id(UUID.randomUUID().toString())
                .signWith(getKey())
                .compact();
    }

    public Claims extractToken(String token) {
        return (Claims) Jwts.parser()
                .verifyWith(getKey())
                .requireIssuer(codeyzerPassConfigurationProperties.getJwtIssuer())
                .build()
                .parse(token)
                .getPayload();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(codeyzerPassConfigurationProperties.getJwtSecret().getBytes());
    }
}
