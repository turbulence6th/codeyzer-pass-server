package com.codeyzer.pass.sunucu.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KullaniciOlusturRequestDTO {
    private String kullaniciKimlik; // SHA512(username + ":" + password)
    private String sifreHash;       // bcrypt(password)
}
