package com.codeyzer.pass.sunucu.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifreYenileDTO {
    private List<HariciSifreYenileElemanDTO> hariciSifreListesi;
    private String kullaniciKimlik;
    private String yeniKullaniciKimlik;
}
