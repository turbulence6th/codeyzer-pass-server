package com.codeyzer.pass.sunucu.service;

import com.codeyzer.pass.sunucu.dto.JwtResponseDTO;
import com.codeyzer.pass.sunucu.dto.KullaniciLoginRequest;
import com.codeyzer.pass.sunucu.dto.KullaniciOlusturRequestDTO;
import com.codeyzer.pass.sunucu.dto.TokenRefreshRequestDTO;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import com.codeyzer.pass.sunucu.exception.CodeyzerPassException;
import com.codeyzer.pass.sunucu.mapper.KullaniciMapper;
import com.codeyzer.pass.sunucu.repository.KullaniciRepository;
import com.codeyzer.pass.sunucu.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final JwtUtil jwtUtil;
    private final KullaniciMapper kullaniciMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Kullanici getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CodeyzerPassException(HttpStatus.UNAUTHORIZED, "Lütfen giriş yapın.");
        }

        return (Kullanici) authentication.getPrincipal();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "kullaniciCache", key = "#kullaniciKimlik")
    public Kullanici getByKimlik(String kullaniciKimlik) {
        return kullaniciRepository.findById(kullaniciKimlik)
                .orElseThrow(() -> new CodeyzerPassException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı."));
    }

    @Transactional
    public JwtResponseDTO kullaniciKaydet(KullaniciOlusturRequestDTO request) {
        if (request.getKullaniciKimlik() == null || request.getSifreHash() == null) {
            throw new CodeyzerPassException(HttpStatus.BAD_REQUEST, "Eksik veri gönderildi.");
        }

        if (kullaniciRepository.existsById(request.getKullaniciKimlik())) {
            throw new CodeyzerPassException(HttpStatus.CONFLICT, "Bu kullanıcı zaten kayıtlı.");
        }

        Kullanici kullanici = kullaniciMapper.toEntity(request);
        kullanici.setSifreHash(passwordEncoder.encode(request.getSifreHash()));
        kullaniciRepository.save(kullanici);

        String accessToken = jwtUtil.generateAccessToken(kullanici.getKullaniciKimlik());
        String refreshToken = jwtUtil.generateRefreshToken(kullanici.getKullaniciKimlik());
        return new JwtResponseDTO(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public JwtResponseDTO kullaniciDogrula(KullaniciLoginRequest request) {
        Kullanici kullanici = kullaniciRepository.findById(request.getKullaniciKimlik())
                .orElseThrow(() -> new CodeyzerPassException(HttpStatus.UNPROCESSABLE_ENTITY, "Kullanıcı bilgileri hatalıdır."));

        boolean sifreGecerli = passwordEncoder.matches(request.getSifreHash(), kullanici.getSifreHash());

        if (!sifreGecerli) {
            throw new CodeyzerPassException(HttpStatus.UNPROCESSABLE_ENTITY, "Kullanıcı bilgileri hatalıdır.");
        }

        String accessToken = jwtUtil.generateAccessToken(kullanici.getKullaniciKimlik());
        String refreshToken = jwtUtil.generateRefreshToken(kullanici.getKullaniciKimlik());
        return new JwtResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public JwtResponseDTO refreshToken(TokenRefreshRequestDTO request) {
        String refreshToken = request.getRefreshToken();
        Claims refreshTokenClaims = jwtUtil.extractToken(refreshToken);
        if (!refreshTokenClaims.getAudience().contains("refresh")) {
            throw new CodeyzerPassException(HttpStatus.UNAUTHORIZED, "Geçersiz audience: refresh token bekleniyordu.");
        }

        String kullaniciKimlik = refreshTokenClaims.getSubject();
        String accessToken = jwtUtil.generateAccessToken(kullaniciKimlik);
        return new JwtResponseDTO(accessToken, refreshToken);
    }
}
