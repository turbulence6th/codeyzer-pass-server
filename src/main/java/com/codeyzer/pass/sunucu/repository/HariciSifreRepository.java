package com.codeyzer.pass.sunucu.repository;

import com.codeyzer.pass.sunucu.entity.HariciSifre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HariciSifreRepository extends JpaRepository<HariciSifre, String> {
    List<HariciSifre> findAllByKullanici_KullaniciKimlik(String kullaniciKimlik);
}
