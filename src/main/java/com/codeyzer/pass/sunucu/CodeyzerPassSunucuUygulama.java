package com.codeyzer.pass.sunucu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CodeyzerPassSunucuUygulama {

	public static void main(String[] args) {
		SpringApplication.run(CodeyzerPassSunucuUygulama.class, args);
	}
}
