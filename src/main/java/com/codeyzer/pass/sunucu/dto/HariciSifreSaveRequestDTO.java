package com.codeyzer.pass.sunucu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HariciSifreSaveRequestDTO {

    private String id;
    private String encryptedData;
    private String encryptedMetadata;
    private String aesIV;
}