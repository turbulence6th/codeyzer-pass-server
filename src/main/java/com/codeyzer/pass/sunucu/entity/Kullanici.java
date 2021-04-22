package com.codeyzer.pass.sunucu.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kullanici {
    @Id
    private String kimlik;
    @OneToMany(mappedBy = "kullanici", cascade = CascadeType.ALL)
    private List<HariciSifre> hariciSifreListesi;
}
