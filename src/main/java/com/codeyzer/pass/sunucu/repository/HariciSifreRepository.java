package com.codeyzer.pass.sunucu.repository;

import com.codeyzer.pass.sunucu.entity.HariciSifre;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HariciSifreRepository extends JpaRepository<HariciSifre, String> {
    List<HariciSifre> findAllByKullanici_KullaniciKimlik(String kullaniciKimlik);
    Optional<HariciSifre> findByIdAndKullanici(String id, Kullanici kullanici);
}
