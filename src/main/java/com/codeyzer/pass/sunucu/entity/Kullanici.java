package com.codeyzer.pass.sunucu.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kullanici {
    @Id
    private String kimlik;
}
