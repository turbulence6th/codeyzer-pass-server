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
    private String kimlik;

    private String icerik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_kimlik")
    private Kullanici kullanici;
}
