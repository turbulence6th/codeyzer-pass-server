package com.codeyzer.pass.sunucu.exception;

import com.codeyzer.pass.sunucu.dto.CodeyzerPassErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CodeyzerPassException.class)
    public ResponseEntity<CodeyzerPassErrorResponseDTO> handleCodeyzerPassException(CodeyzerPassException ex, HttpServletRequest request) {
        CodeyzerPassErrorResponseDTO responseDTO = CodeyzerPassErrorResponseDTO.builder()
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .status(ex.getHttpStatus().value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(responseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CodeyzerPassErrorResponseDTO> handleGeneralException(HttpServletRequest request) {
        CodeyzerPassErrorResponseDTO responseDTO = CodeyzerPassErrorResponseDTO.builder()
                .error("Beklenmedik bir hata oluştu.")
                .path(request.getRequestURI())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseDTO);
    }
}
