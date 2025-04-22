package com.codeyzer.pass.sunucu.controller;

import com.codeyzer.pass.sunucu.dto.*;
import com.codeyzer.pass.sunucu.service.KullaniciService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kullanici")
@RequiredArgsConstructor
public class KullaniciController {

    private final KullaniciService kullaniciService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@RequestBody KullaniciOlusturRequestDTO request) {
        JwtResponseDTO responseDTO = kullaniciService.kullaniciKaydet(request);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody KullaniciLoginRequest request) {
        JwtResponseDTO responseDTO = kullaniciService.kullaniciDogrula(request);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody TokenRefreshRequestDTO request) {
        return ResponseEntity.ok(kullaniciService.refreshToken(request));
    }

    @PutMapping("/sifre-guncelle")
    public ResponseEntity<JwtResponseDTO> sifreGuncelle(@RequestBody SifreGuncelleRequestDTO request) {
        JwtResponseDTO responseDTO = kullaniciService.sifreGuncelle(request);
        return ResponseEntity.ok(responseDTO);
    }
}
