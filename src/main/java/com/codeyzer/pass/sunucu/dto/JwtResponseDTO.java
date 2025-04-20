package com.codeyzer.pass.sunucu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {

    private String accessToken;
    private String refreshToken;
}
