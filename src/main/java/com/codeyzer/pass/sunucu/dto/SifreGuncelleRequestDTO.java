package com.codeyzer.pass.sunucu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SifreGuncelleRequestDTO {
    private String yeniKullaniciKimlik;
    private String yeniSifreHash;
    private List<SifreGuncelleHariciSifreDTO> yeniHariciSifreList; // Adı böyle olmalı
}