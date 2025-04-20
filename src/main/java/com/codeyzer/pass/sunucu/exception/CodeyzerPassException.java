package com.codeyzer.pass.sunucu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class CodeyzerPassException extends RuntimeException {

    private final HttpStatus httpStatus;

    public CodeyzerPassException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
