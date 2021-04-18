package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifreGetirDTO {
    private String platform;
    private String kullaniciKimlik;
}
