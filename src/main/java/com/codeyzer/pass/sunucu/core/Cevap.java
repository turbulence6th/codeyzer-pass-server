package com.codeyzer.pass.sunucu.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Cevap<T> {

    private T sonuc;
    private Boolean basarili;
    private String mesaj;

    public Cevap(T sonuc, String mesaj) {
        this(sonuc, true, mesaj);
    }
}
