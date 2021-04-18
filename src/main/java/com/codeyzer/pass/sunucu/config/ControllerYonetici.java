package com.codeyzer.pass.sunucu.config;

import com.codeyzer.pass.sunucu.core.Cevap;
import com.codeyzer.pass.sunucu.exception.CodeyzerIstisna;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerYonetici {

    @ExceptionHandler(CodeyzerIstisna.class)
    public Cevap<?> codeyzerIstisna(CodeyzerIstisna ex) {
        return new Cevap<>(ex.getNesne(), false, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Cevap<?> exception(Exception ex) {
        return new Cevap<>(null, false, "Beklenmedik bir hata oluştu.");
    }
}
