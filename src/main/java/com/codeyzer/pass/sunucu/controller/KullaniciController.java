package com.codeyzer.pass.sunucu.controller;

import com.codeyzer.pass.sunucu.core.Cevap;
import com.codeyzer.pass.sunucu.dto.KullaniciDogrulaDTO;
import com.codeyzer.pass.sunucu.dto.KullaniciOlusturDTO;
import com.codeyzer.pass.sunucu.servis.KullaniciServis;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/kullanici")
public class KullaniciController {

    private final KullaniciServis kullaniciServis;

    public KullaniciController(KullaniciServis kullaniciServis) {
        this.kullaniciServis = kullaniciServis;
    }

    @RequestMapping(path = "/yeni", method = RequestMethod.POST)
    public Cevap<String> yeni(@RequestBody KullaniciOlusturDTO istek) {
        kullaniciServis.kullaniciOlustur(istek);
        return new Cevap<>(istek.getKimlik(), "Kullanıcı başarılı bir şekilde oluşturuldu.");
    }

    @RequestMapping(path = "/dogrula", method = RequestMethod.POST)
    public Cevap<String> yeni(@RequestBody KullaniciDogrulaDTO istek) {
        kullaniciServis.kullaniciDogrula(istek);
        return new Cevap<>(istek.getKimlik(), "Kullanıcı başarılı bir şekilde doğrulandı.");
    }
}
