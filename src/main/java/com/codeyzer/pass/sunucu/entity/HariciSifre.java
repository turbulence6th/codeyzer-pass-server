package com.codeyzer.pass.sunucu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "harici_sifre")
public class HariciSifre {

    @Id
    private String id;

    // Şifreli olarak saklanan kullanıcı adı, şifre ve varsa not
    @Column(name = "encrypted_data", nullable = false, columnDefinition = "TEXT")
    private String encryptedData;

    // Şifreli metadata: url, androidPackage vb.
    @Column(name = "encrypted_metadata", nullable = false, columnDefinition = "TEXT")
    private String encryptedMetadata;

    @Column(name = "aes_iv_data", nullable = false, columnDefinition = "TEXT")
    private String aesIVData;

    @Column(name = "aes_iv_metadata", nullable = false, columnDefinition = "TEXT")
    private String aesIVMetadata;

    // Sahiplik ilişkisi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_kimlik", nullable = false)
    private Kullanici kullanici;
}