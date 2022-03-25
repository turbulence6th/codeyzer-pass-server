package com.codeyzer.pass.sunucu.controller;

import com.codeyzer.pass.sunucu.core.Cevap;
import com.codeyzer.pass.sunucu.dto.*;
import com.codeyzer.pass.sunucu.servis.api.HariciDosyaServis;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/hariciDosya")
@RequiredArgsConstructor
public class HariciDosyaController {

    private final HariciDosyaServis hariciDosyaServis;

    @RequestMapping(path = "/yukle", method = RequestMethod.POST)
    public Cevap<String> yukle(@RequestParam("dosya") MultipartFile dosya) throws IOException {
        String mongoId = hariciDosyaServis.dosyaYukle(dosya.getInputStream());
        return new Cevap<>(mongoId, "http.hariciDosya.yukle");
    }

    @RequestMapping(path = "/indir", method = RequestMethod.POST)
    public void indir(@RequestBody HariciDosyaIndirDTO hariciDosyaIndirDTO, HttpServletResponse response) throws IOException {
        hariciDosyaServis.dosyaIndir(hariciDosyaIndirDTO, response.getOutputStream());
    }

    @RequestMapping(path = "/getir", method = RequestMethod.POST)
    public Cevap<List<HariciDosyaDTO>> getir(@RequestBody HariciDosyaGetirDTO istek) {
        List<HariciDosyaDTO> hariciDosyaDTOListesi = hariciDosyaServis.hariciDosyaGetir(istek);
        return new Cevap<>(hariciDosyaDTOListesi, "http.hariciDosya.getir");
    }

    @RequestMapping(path = "/kaydet", method = RequestMethod.POST)
    public Cevap<Void> kaydet(@RequestBody HariciDosyaKaydetDTO istek) {
        hariciDosyaServis.hariciDosyaKaydet(istek);
        return new Cevap<>(null, "http.hariciDosya.kaydet");
    }

//    @RequestMapping(path = "/guncelle", method = RequestMethod.POST)
//    public Cevap<Void> guncelle(@RequestBody HariciSifreGuncelleDTO istek) {
//        hariciSifreServis.hariciSifreGuncelle(istek);
//        return new Cevap<>(null, "http.hariciSifre.guncelle");
//    }
//
//    @RequestMapping(path = "/sil", method = RequestMethod.POST)
//    public Cevap<Void> sil(@RequestBody HariciSifreSilDTO istek) {
//        hariciSifreServis.hariciSifreSil(istek);
//        return new Cevap<>(null, "http.hariciSifre.sil");
//    }
}
