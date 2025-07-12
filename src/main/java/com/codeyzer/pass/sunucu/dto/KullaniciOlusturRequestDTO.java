package com.codeyzer.pass.sunucu.dto;

import lombok.Data;

@Data
public class KullaniciOlusturRequestDTO {
    private String kullaniciKimlik; // SHA512(username + ":" + password)
    private String sifreSha512;       // bcrypt(password)
}
