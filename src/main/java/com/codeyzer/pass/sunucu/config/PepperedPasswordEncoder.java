package com.codeyzer.pass.sunucu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class PepperedPasswordEncoder implements PasswordEncoder {

    private final CodeyzerPassConfigurationProperties codeyzerPassConfigurationProperties;

    @Override
    public String encode(CharSequence rawPassword) {
        String pepperedPassword = pepperPassword(rawPassword.toString());
        return BCrypt.hashpw(pepperedPassword, BCrypt.gensalt(codeyzerPassConfigurationProperties.getBcryptStrength()));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String pepperedPassword = pepperPassword(rawPassword.toString());
        return BCrypt.checkpw(pepperedPassword, encodedPassword);
    }

    private String pepperPassword(String password) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(codeyzerPassConfigurationProperties.getPepper().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKey);
            byte[] hmacBytes = hmac.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Şifre pepper'lama işlemi başarısız oldu", e);
        }
    }
}
