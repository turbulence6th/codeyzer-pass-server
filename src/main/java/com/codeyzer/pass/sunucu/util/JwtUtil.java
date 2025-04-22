package com.codeyzer.pass.sunucu.util;

import com.codeyzer.pass.sunucu.config.CodeyzerPassConfigurationProperties;
import com.codeyzer.pass.sunucu.entity.RefreshToken;
import com.codeyzer.pass.sunucu.repository.KullaniciRepository;
import com.codeyzer.pass.sunucu.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final CodeyzerPassConfigurationProperties codeyzerPassConfigurationProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KullaniciRepository kullaniciRepository;

    public String generateAccessToken(String kullaniciKimlik) {
        return generateToken(kullaniciKimlik, "access", new Date(System.currentTimeMillis() + codeyzerPassConfigurationProperties.getAccessTokenExpiration()));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public String generateRefreshToken(String kullaniciKimlik) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[64];
        secureRandom.nextBytes(tokenBytes);
        String refreshTokenId = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(refreshTokenId);
        refreshToken.setKullanici(kullaniciRepository.getReferenceById(kullaniciKimlik));
        refreshToken.setExpiryDate(LocalDateTime.now().plus(codeyzerPassConfigurationProperties.getRefreshTokenExpiration(), ChronoUnit.MILLIS));
        refreshTokenRepository.save(refreshToken);

        return refreshTokenId;
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
