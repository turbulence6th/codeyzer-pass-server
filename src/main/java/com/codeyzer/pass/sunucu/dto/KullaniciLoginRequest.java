package com.codeyzer.pass.sunucu.dto;

import lombok.Data;

@Data
public class KullaniciLoginRequest {
    private String kullaniciKimlik; // SHA512(username + ":" + password)
    private String sifreHash;       // bcrypt(password) // frontendde yapılır
}
