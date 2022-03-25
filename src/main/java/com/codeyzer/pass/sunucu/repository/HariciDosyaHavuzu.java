package com.codeyzer.pass.sunucu.repository;

import com.codeyzer.pass.sunucu.entity.HariciDosya;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HariciDosyaHavuzu extends JpaRepository<HariciDosya, String> {

    @Query("SELECT hd FROM HariciDosya hd WHERE hd.kimlik = :kimlik AND hd.kullanici = :kullanici")
    Optional<HariciDosya> kimlikVeKullaniyaGoreGetir(@Param("kimlik") String kimlik, @Param("kullanici") Kullanici kullanici);

    @Query("SELECT hd FROM HariciDosya hd WHERE hd.kullanici = :kullanici")
    List<HariciDosya> kullaniciIleGetir(@Param("kullanici") Kullanici kullanici);

    @Query("SELECT hd FROM HariciDosya hd WHERE hd.mongoKimlik = :mongoKimlik")
    Optional<HariciDosya> mongoKimlikIleGetir(@Param("mongoKimlik") String mongoKimlik);
}
