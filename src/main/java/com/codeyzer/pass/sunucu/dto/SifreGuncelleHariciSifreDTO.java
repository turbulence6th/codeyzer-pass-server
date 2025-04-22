package com.codeyzer.pass.sunucu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Bu DTO, hangi eski kaydın yerine geçeceğini ve yeni veriyi taşır
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SifreGuncelleHariciSifreDTO {
    private String eskiId; // Değiştirilecek eski kaydın ID'si
    // Yeni şifrelenmiş veriler (HariciSifreDTO'dan alınabilir veya doğrudan yazılabilir)
    private String id;
    private String encryptedData;
    private String encryptedMetadata;
    private String aesIV;
}
