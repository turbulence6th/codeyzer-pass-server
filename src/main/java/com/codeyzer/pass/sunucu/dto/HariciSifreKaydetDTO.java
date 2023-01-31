package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifreKaydetDTO {
    private String icerik;
    private String kullaniciKimlik;
}
