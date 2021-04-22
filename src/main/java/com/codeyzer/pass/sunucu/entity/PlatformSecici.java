package com.codeyzer.pass.sunucu.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformSecici {
    @Id
    private String kimlik;
    private String platform;
    private String platformRegex;
    private String kullaniciAdiSecici;
    private String sifreSecici;
}
