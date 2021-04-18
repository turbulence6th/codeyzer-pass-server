package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifreSilDTO {
    private String kimlik;
    private String kullaniciKimlik;
}
