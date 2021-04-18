package com.codeyzer.pass.sunucu.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifre {
    @Id
    private String kimlik;
    private String platform;
    private String kullaniciAdi;
    private String sifre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_kimlik")
    private Kullanici kullanici;
}
