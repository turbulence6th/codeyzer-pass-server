package com.codeyzer.pass.sunucu.repository;

import com.codeyzer.pass.sunucu.entity.HariciSifre;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HariciSifreHavuzu extends JpaRepository<HariciSifre, String> {

    @Query("SELECT hs FROM HariciSifre hs WHERE hs.kimlik = :kimlik AND hs.kullanici = :kullanici")
    Optional<HariciSifre> kimlikVeKullaniyaGoreGetir(@Param("kimlik") String kimlik, @Param("kullanici") Kullanici kullanici);

    @Query("SELECT hs FROM HariciSifre hs WHERE hs.kullanici = :kullanici")
    List<HariciSifre> kullaniciIleGetir(@Param("kullanici") Kullanici kullanici);
}
