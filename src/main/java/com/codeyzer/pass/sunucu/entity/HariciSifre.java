package com.codeyzer.pass.sunucu.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifre {
    @Id
    @GeneratedValue(generator = "kimlik_uretici")
    @GenericGenerator(name = "kimlik_uretici", strategy = "com.codeyzer.pass.sunucu.config.KimlikUretici")
    private String kimlik;

    private String icerik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_kimlik")
    private Kullanici kullanici;
}
