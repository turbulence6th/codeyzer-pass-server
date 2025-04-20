package com.codeyzer.pass.sunucu.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KullaniciDogrulaDTO {
    private String kullaniciKimlik;
    private String sifreHash;
}
