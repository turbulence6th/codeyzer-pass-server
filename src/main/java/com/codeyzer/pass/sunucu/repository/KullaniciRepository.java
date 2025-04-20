package com.codeyzer.pass.sunucu.repository;

import com.codeyzer.pass.sunucu.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KullaniciRepository extends JpaRepository<Kullanici, String> {
}
