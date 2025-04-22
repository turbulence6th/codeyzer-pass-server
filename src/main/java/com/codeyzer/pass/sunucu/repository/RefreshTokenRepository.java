package com.codeyzer.pass.sunucu.repository;

import com.codeyzer.pass.sunucu.entity.Kullanici;
import com.codeyzer.pass.sunucu.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> { // ID tipi String (jti)

    // Kullanıcıya göre token bulma (opsiyonel)
    Optional<RefreshToken> findByKullanici(Kullanici kullanici);

    // Token string'ine göre bulma metodu kaldırıldı.
    // Optional<RefreshToken> findByToken(String token);

    // JTI (ID) ile varlık kontrolü için existsById metodu JpaRepository'den gelir.

    // Kullanıcıya ait tüm tokenları silmek için metod (sifreGuncelle'de lazım olacak)
    @Modifying // Silme veya güncelleme işlemi olduğunu belirtir
    @Query("DELETE FROM RefreshToken rt WHERE rt.kullanici = :kullanici")
    void deleteByKullanici(@Param("kullanici") Kullanici kullanici);

    // Süresi dolmuş tokenları silmek için metod (opsiyonel, zamanlanmış görev için)
    // void deleteByExpiryDateBefore(Instant now);
} 