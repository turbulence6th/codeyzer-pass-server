package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifreKaydetDTO {
    private String kimlik;
    private String platform;
    private String kullaniciAdi;
    private String sifre;
    private String kullaniciKimlik;
}
