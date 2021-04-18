package com.codeyzer.pass.sunucu.repository;

import com.codeyzer.pass.sunucu.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KullaniciHavuzu extends JpaRepository<Kullanici, String> {

    @Query("SELECT k FROM Kullanici k WHERE k.kimlik = :kimlik")
    Optional<Kullanici> kimlikIleGetir(@Param("kimlik") String kimlik);
}
