package com.codeyzer.pass.sunucu.config;

import com.codeyzer.pass.sunucu.core.Cevap;
import com.codeyzer.pass.sunucu.exception.CodeyzerIstisna;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class ControllerYonetici {

    @ExceptionHandler(CodeyzerIstisna.class)
    public Cevap<?> codeyzerIstisna(CodeyzerIstisna ex) {
        log.log(Level.ERROR, ex);
        return new Cevap<>(ex.getNesne(), false, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Cevap<?> exception(Exception ex) {
        log.log(Level.ERROR, ex);
        return new Cevap<>(null, false, "http.server.hata.beklenmedik");
    }
}
