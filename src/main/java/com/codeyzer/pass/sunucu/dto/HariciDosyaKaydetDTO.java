package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciDosyaKaydetDTO {
    private String mongoKimlik;
    private String metaVeri;
    private String kullaniciKimlik;
}
