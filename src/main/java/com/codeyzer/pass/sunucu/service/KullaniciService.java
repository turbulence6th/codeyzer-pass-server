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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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

@Slf4j
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
    private final CacheManager cacheManager;

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
        if (request.getKullaniciKimlik() == null || request.getSifreSha512() == null) {
            throw new CodeyzerPassException(HttpStatus.BAD_REQUEST, "Eksik veri gönderildi.");
        }

        if (kullaniciRepository.existsById(request.getKullaniciKimlik())) {
            throw new CodeyzerPassException(HttpStatus.CONFLICT, "Bu kullanıcı zaten kayıtlı.");
        }

        Kullanici kullanici = kullaniciMapper.toEntity(request);
        kullanici.setSifreHash(passwordEncoder.encode(request.getSifreSha512()));
        Kullanici kaydedilenKullanici = kullaniciRepository.save(kullanici);

        String accessToken = jwtUtil.generateAccessToken(kaydedilenKullanici.getKullaniciKimlik());
        String refreshTokenString = jwtUtil.generateRefreshToken(kaydedilenKullanici.getKullaniciKimlik());

        return new JwtResponseDTO(accessToken, refreshTokenString);
    }

    @Transactional
    public JwtResponseDTO kullaniciDogrula(KullaniciLoginRequest request) {
        Kullanici kullanici = kullaniciRepository.findById(request.getKullaniciKimlik())
                .orElseThrow(() -> new CodeyzerPassException(HttpStatus.UNPROCESSABLE_ENTITY, "Kullanıcı bilgileri hatalıdır."));

        boolean sifreGecerli = passwordEncoder.matches(request.getSifreSha512(), kullanici.getSifreHash());

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

    @Transactional
    public void logout(TokenRefreshRequestDTO request) {
        String refreshTokenId = request.getRefreshToken();

        // Refresh token'ı bul
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new CodeyzerPassException(HttpStatus.UNAUTHORIZED, "Refresh token bulunamadı veya geçersiz kılındı."));

        // Token'dan kullanıcıyı al
        Kullanici kullanici = refreshToken.getKullanici();
        String kullaniciKimlik = kullanici.getKullaniciKimlik();

        // Sadece bu refresh token'ı sil
        refreshTokenRepository.delete(refreshToken);

        log.info("Kullanıcı çıkış yaptı. Refresh token iptal edildi. Kullanıcı: {}",
                kullaniciKimlik.substring(0, Math.min(8, kullaniciKimlik.length())) + "...");
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public JwtResponseDTO sifreGuncelle(SifreGuncelleRequestDTO request) {
        // 1. Mevcut kullanıcıyı al
        Kullanici mevcutKullanici = getCurrentUser();
        String eskiKullaniciKimlik = mevcutKullanici.getKullaniciKimlik();

        log.info("Ana şifre güncelleme işlemi başlatıldı. Kullanıcı: {}", eskiKullaniciKimlik.substring(0, Math.min(8, eskiKullaniciKimlik.length())) + "...");

        // 2. Validasyon: Yeni kimlik ile eski kimlik aynı olamaz
        if (eskiKullaniciKimlik.equals(request.getYeniKullaniciKimlik())) {
            log.warn("Ana şifre güncelleme başarısız: Yeni kimlik eski kimlik ile aynı. Kullanıcı: {}", eskiKullaniciKimlik.substring(0, Math.min(8, eskiKullaniciKimlik.length())) + "...");
            throw new CodeyzerPassException(HttpStatus.BAD_REQUEST,
                    "Yeni ana şifre, mevcut ana şifre ile aynı olamaz.");
        }

        // 3. Validasyon: Request verileri kontrolü
        if (request.getYeniKullaniciKimlik() == null || request.getYeniSifreSha512() == null) {
            log.error("Ana şifre güncelleme başarısız: Eksik veri. Kullanıcı: {}", eskiKullaniciKimlik.substring(0, Math.min(8, eskiKullaniciKimlik.length())) + "...");
            throw new CodeyzerPassException(HttpStatus.BAD_REQUEST, "Eksik veri gönderildi.");
        }

        // 4. Validasyon: Tüm harici şifrelerin mevcut kullanıcıya ait olduğunu doğrula
        List<String> eskiIdList = request.getYeniHariciSifreList().stream()
                .map(SifreGuncelleHariciSifreDTO::getEskiId)
                .collect(Collectors.toList());

        List<HariciSifre> mevcutHariciSifreler = hariciSifreRepository.findAllByKullanici_KullaniciKimlik(eskiKullaniciKimlik);

        if (!eskiIdList.isEmpty() && mevcutHariciSifreler.size() != eskiIdList.size()) {
            log.error("Ana şifre güncelleme başarısız: Şifre sayısı uyuşmazlığı. Beklenen: {}, Gönderilen: {}",
                     mevcutHariciSifreler.size(), eskiIdList.size());
            throw new CodeyzerPassException(HttpStatus.BAD_REQUEST,
                    "Gönderilen şifre sayısı ile mevcut şifre sayısı eşleşmiyor. Beklenen: "
                    + mevcutHariciSifreler.size() + ", Gönderilen: " + eskiIdList.size());
        }

        log.debug("Validasyon başarılı. {} adet şifre kaydı yeniden şifrelenecek.", mevcutHariciSifreler.size());

        // 5. Yeni kullanıcı kaydını oluştur
        KullaniciOlusturRequestDTO yeniKullaniciDto = kullaniciMapper.toKullaniciOlusturRequestDTO(request);
        JwtResponseDTO jwtResponseDTO = kullaniciKaydet(yeniKullaniciDto);

        // 6. Yeni kullanıcıyı getir
        Kullanici yeniKaydedilenKullanici = kullaniciRepository.getReferenceById(request.getYeniKullaniciKimlik());

        // 7. Tüm harici şifreleri yeni kullanıcıya taşı
        for (SifreGuncelleHariciSifreDTO sifreDTO : request.getYeniHariciSifreList()) {
            HariciSifre eskiHariciSifre = hariciSifreRepository.findByIdAndKullanici(sifreDTO.getEskiId(), mevcutKullanici)
                    .orElseThrow(() -> new CodeyzerPassException(HttpStatus.BAD_REQUEST,
                            "İşlenecek eski harici şifre bulunamadı: ID " + sifreDTO.getEskiId()));

            // Yeni harici şifre oluştur
            HariciSifre yeniHariciSifre = hariciSifreMapper.toEntity(sifreDTO);
            yeniHariciSifre.setKullanici(yeniKaydedilenKullanici);
            hariciSifreRepository.save(yeniHariciSifre);

            // Eski kaydı sil
            hariciSifreRepository.delete(eskiHariciSifre);
        }

        // 8. Güvenlik kontrolü: Eski kullanıcıya ait atık kayıt kalmamalı
        List<HariciSifre> atikHariciSifreListe = hariciSifreRepository.findAllByKullanici_KullaniciKimlik(eskiKullaniciKimlik);
        if (!atikHariciSifreListe.isEmpty()) {
            log.error("Ana şifre güncelleme başarısız: Atık kayıt bulundu. Sayı: {}", atikHariciSifreListe.size());
            throw new CodeyzerPassException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Harici şifre taşıma işlemi tutarsız. Eski kullanıcıya ait atık kayıtlar bulundu: " + atikHariciSifreListe.size());
        }

        // 9. Eski kullanıcının tüm refresh token'larını sil
        refreshTokenRepository.deleteByKullanici(mevcutKullanici);
        log.debug("Eski kullanıcının refresh token'ları silindi.");

        // 10. Eski kullanıcı kaydını sil
        kullaniciRepository.delete(mevcutKullanici);
        log.info("Ana şifre başarıyla güncellendi. Eski kullanıcı: {}, Yeni kullanıcı: {}",
                eskiKullaniciKimlik.substring(0, Math.min(8, eskiKullaniciKimlik.length())) + "...",
                request.getYeniKullaniciKimlik().substring(0, Math.min(8, request.getYeniKullaniciKimlik().length())) + "...");

        // 11. Cache'i temizle (hem eski hem yeni kullanıcı için)
        Cache kullaniciCache = cacheManager.getCache("kullaniciCache");
        if (kullaniciCache != null) {
            kullaniciCache.evict(eskiKullaniciKimlik);
            kullaniciCache.evict(request.getYeniKullaniciKimlik());
            log.debug("Cache temizlendi. Eski kullanıcı: {}, Yeni kullanıcı: {}",
                    eskiKullaniciKimlik.substring(0, Math.min(8, eskiKullaniciKimlik.length())) + "...",
                    request.getYeniKullaniciKimlik().substring(0, Math.min(8, request.getYeniKullaniciKimlik().length())) + "...");
        }

        return jwtResponseDTO;
    }
}
