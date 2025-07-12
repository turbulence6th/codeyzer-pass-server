package com.codeyzer.pass.sunucu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HariciSifreDTO {

    private String id;
    private String encryptedData;
    private String encryptedMetadata;
    private String aesIVData;
    private String aesIVMetadata;
}
