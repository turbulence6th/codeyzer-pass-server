package com.codeyzer.pass.sunucu.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeyzerPassErrorResponseDTO {

    private String error;
    private String path;
    private Integer status;
    private LocalDateTime timestamp;
}
