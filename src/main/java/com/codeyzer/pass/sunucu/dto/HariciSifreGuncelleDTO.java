package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifreGuncelleDTO {
    private String kimlik;
    private String icerik;
    private String kullaniciKimlik;
}
