package com.codeyzer.pass.sunucu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "codeyzer-pass")
@Getter
@Setter
public class CodeyzerPassConfigurationProperties {

    private String pepper;
    private int bcryptStrength;
    private String jwtSecret;
    private String jwtIssuer;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}
