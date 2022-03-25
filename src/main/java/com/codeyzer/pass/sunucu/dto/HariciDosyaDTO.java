package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciDosyaDTO {
    private String kimlik;
    private String mongoKimlik;
    private String metaVeri;
}
