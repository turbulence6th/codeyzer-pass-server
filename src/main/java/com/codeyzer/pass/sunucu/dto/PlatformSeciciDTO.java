package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformSeciciDTO {
    private String platform;
    private String platformRegex;
    private String kullaniciAdiSecici;
    private String sifreSecici;
}
