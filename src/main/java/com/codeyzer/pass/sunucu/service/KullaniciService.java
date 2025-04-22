package com.codeyzer.pass.sunucu.service;

import com.codeyzer.pass.sunucu.dto.*;
import com.codeyzer.pass.sunucu.entity.HariciSifre;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import com.codeyzer.pass.sunucu.entity.RefreshToken;
import com.codeyzer.pass.sunucu.exception.CodeyzerPassException;
import com.codeyzer.pass.sunucu.mapper.HariciSifreMapper;
import com.codeyzer.pass.sunucu.mapper.KullaniciMapper;
import com.codeyzer.pass.sunucu.repository.HariciSifreRepository;
import com.codeyzer.pass.sunucu.repository.KullaniciRepository;
import com.codeyzer.pass.sunucu.repository.RefreshTokenRepository;
import com.codeyzer.pass.sunucu.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KullaniciService {

    private final KullaniciRepository kullaniciRepository;
    private final HariciSifreRepository hariciSifreRepository;
    private final HariciSifreMapper hariciSifreMapper;
    private final JwtUtil jwtUtil;
    private final KullaniciMapper kullaniciMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public Kullanici getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CodeyzerPassException(HttpStatus.UNAUTHORIZED, "Lütfen giriş yapın.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Kullanici) {
            return (Kullanici) principal;
        } else if (principal instanceof UserDetails) {
            String kullaniciKimlik = ((UserDetails) principal).getUsername();
            return getByKimlik(kullaniciKimlik);
        } else if (principal instanceof String && !principal.equals("anonymousUser")) {
            return getByKimlik((String) principal);
        } else {
            throw new CodeyzerPassException(HttpStatus.UNAUTHORIZED, "Geçerli kullanıcı oturumu bulunamadı.");
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "kullaniciCache", key = "#kullaniciKimlik")
    public Kullanici getByKimlik(String kullaniciKimlik) {
        return kullaniciRepository.findById(kullaniciKimlik)
                .orElseThrow(() -> new CodeyzerPassException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı: " + kullaniciKimlik));
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
        Kullanici kaydedilenKullanici = kullaniciRepository.save(kullanici);

        String accessToken = jwtUtil.generateAccessToken(kaydedilenKullanici.getKullaniciKimlik());
        String refreshTokenString = jwtUtil.generateRefreshToken(kaydedilenKullanici.getKullaniciKimlik());

        return new JwtResponseDTO(accessToken, refreshTokenString);
    }

    @Transactional
    public JwtResponseDTO kullaniciDogrula(KullaniciLoginRequest request) {
        Kullanici kullanici = kullaniciRepository.findById(request.getKullaniciKimlik())
                .orElseThrow(() -> new CodeyzerPassException(HttpStatus.UNPROCESSABLE_ENTITY, "Kullanıcı bilgileri hatalıdır."));

        boolean sifreGecerli = passwordEncoder.matches(request.getSifreHash(), kullanici.getSifreHash());

        if (!sifreGecerli) {
            throw new CodeyzerPassException(HttpStatus.UNPROCESSABLE_ENTITY, "Kullanıcı bilgileri hatalıdır.");
        }

        String accessToken = jwtUtil.generateAccessToken(kullanici.getKullaniciKimlik());
        String refreshTokenString = jwtUtil.generateRefreshToken(kullanici.getKullaniciKimlik());

        return new JwtResponseDTO(accessToken, refreshTokenString);
    }

    @Transactional
    public JwtResponseDTO refreshToken(TokenRefreshRequestDTO request) {
        String refreshTokenId = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new CodeyzerPassException(HttpStatus.UNAUTHORIZED, "Refresh token bulunamadı veya geçersiz kılındı."));

        String kullaniciKimlik = refreshToken.getKullanici().getKullaniciKimlik();

        refreshTokenRepository.delete(refreshToken);

        String yeniAccessToken = jwtUtil.generateAccessToken(kullaniciKimlik);
        String yeniRefreshTokenString = jwtUtil.generateRefreshToken(kullaniciKimlik);

        return new JwtResponseDTO(yeniAccessToken, yeniRefreshTokenString);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
//    @CacheEvict(value = "kullaniciCache", key = "#mevcutKullanici.kullaniciKimlik")
    public JwtResponseDTO sifreGuncelle(SifreGuncelleRequestDTO request) {
        Kullanici mevcutKullanici = getCurrentUser();
        String eskiKullaniciKimlik = mevcutKullanici.getKullaniciKimlik();

        KullaniciOlusturRequestDTO yeniKullaniciDto = kullaniciMapper.toKullaniciOlusturRequestDTO(request);

        JwtResponseDTO jwtResponseDTO = kullaniciKaydet(yeniKullaniciDto);

        Kullanici yeniKaydedilenKullanici = kullaniciRepository.getReferenceById(request.getYeniKullaniciKimlik());

        for (SifreGuncelleHariciSifreDTO sifreDTO : request.getYeniHariciSifreList()) {
            HariciSifre eskiHariciSifre = hariciSifreRepository.findByIdAndKullanici(sifreDTO.getEskiId(), mevcutKullanici)
                    .orElseThrow(() -> new CodeyzerPassException(HttpStatus.BAD_REQUEST,
                            "İşlenecek eski harici şifre bulunamadı: ID " + sifreDTO.getEskiId()));
            hariciSifreRepository.delete(eskiHariciSifre);

            HariciSifre yeniHariciSifre = hariciSifreMapper.toEntity(sifreDTO);
            yeniHariciSifre.setKullanici(yeniKaydedilenKullanici);
            hariciSifreRepository.save(yeniHariciSifre);
        }

        List<HariciSifre> atikHariciSifreListe = hariciSifreRepository.findAllByKullanici_KullaniciKimlik(eskiKullaniciKimlik);
        if (!atikHariciSifreListe.isEmpty()) {
            throw new CodeyzerPassException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Harici şifre taşıma işlemi tutarsız. Eski kullanıcıya ait atık kayıtlar bulundu.");
        }

        refreshTokenRepository.deleteByKullanici(mevcutKullanici);
        kullaniciRepository.delete(mevcutKullanici);

        return jwtResponseDTO;
    }
}
