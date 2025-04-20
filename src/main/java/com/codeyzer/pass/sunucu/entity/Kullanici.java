package com.codeyzer.pass.sunucu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "kullanici")
public class Kullanici {

    @Id
    @Column(name = "kullanici_kimlik", unique = true, nullable = false)
    private String kullaniciKimlik; // Örn: SHA512(username + ':' + password)

    @Column(name = "sifre_hash", nullable = false)
    private String sifreHash; // bcyrypt
}