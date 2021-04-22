package com.codeyzer.pass.sunucu.repository;

import com.codeyzer.pass.sunucu.entity.PlatformSecici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface PlatformSeciciHavuzu extends JpaRepository<PlatformSecici, String> {

    @Query("SELECT P FROM PlatformSecici P")
    Stream<PlatformSecici> hepsiniGetir();
}
