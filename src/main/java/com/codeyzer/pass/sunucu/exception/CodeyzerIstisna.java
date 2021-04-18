package com.codeyzer.pass.sunucu.exception;

import lombok.Getter;

@Getter
public class CodeyzerIstisna extends RuntimeException {

    private Object nesne;

    public CodeyzerIstisna(String message) {
        super(message);
    }

    public CodeyzerIstisna(String message, Object nesne) {
        this(message);
        this.nesne = nesne;
    }
}
