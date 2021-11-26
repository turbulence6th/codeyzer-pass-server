package com.codeyzer.pass.sunucu.controller;

import com.codeyzer.pass.sunucu.core.Cevap;
import com.codeyzer.pass.sunucu.dto.*;
import com.codeyzer.pass.sunucu.servis.api.HariciSifreServis;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/hariciSifre")
public class HariciSifreController {

    private final HariciSifreServis hariciSifreServis;

    public HariciSifreController(HariciSifreServis hariciSifreServis) {
        this.hariciSifreServis = hariciSifreServis;
    }

    @RequestMapping(path = "/getir", method = RequestMethod.POST)
    public Cevap<List<HariciSifreDTO>> getir(@RequestBody HariciSifreGetirDTO istek) {
        List<HariciSifreDTO> hariciSifreDTOListesi = hariciSifreServis.hariciSifreGetir(istek);
        return new Cevap<>(hariciSifreDTOListesi, "http.hariciSifre.getir");
    }

    @RequestMapping(path = "/kaydet", method = RequestMethod.POST)
    public Cevap<Void> kaydet(@RequestBody HariciSifreKaydetDTO istek) {
        hariciSifreServis.hariciSifreKaydet(istek);
        return new Cevap<>(null, "http.hariciSifre.kaydet");
    }

    @RequestMapping(path = "/guncelle", method = RequestMethod.POST)
    public Cevap<Void> guncelle(@RequestBody HariciSifreGuncelleDTO istek) {
        hariciSifreServis.hariciSifreGuncelle(istek);
        return new Cevap<>(null, "http.hariciSifre.guncelle");
    }

    @RequestMapping(path = "/sil", method = RequestMethod.POST)
    public Cevap<Void> sil(@RequestBody HariciSifreSilDTO istek) {
        hariciSifreServis.hariciSifreSil(istek);
        return new Cevap<>(null, "http.hariciSifre.sil");
    }

    @RequestMapping(path = "/yenile", method = RequestMethod.POST)
    public Cevap<Void> sifreleriYenile(@RequestBody HariciSifreYenileDTO istek) {
        hariciSifreServis.hariciSifreYenile(istek);
        return new Cevap<>(null, "http.hariciSifre.yenile");
    }
}
