package com.codeyzer.pass.sunucu.servis.impl;

import com.codeyzer.pass.sunucu.dto.*;
import com.codeyzer.pass.sunucu.entity.HariciSifre;
import com.codeyzer.pass.sunucu.exception.CodeyzerIstisna;
import com.codeyzer.pass.sunucu.mapper.HariciSifreMapper;
import com.codeyzer.pass.sunucu.repository.HariciSifreHavuzu;
import com.codeyzer.pass.sunucu.repository.KullaniciHavuzu;
import com.codeyzer.pass.sunucu.servis.api.HariciSifreServis;
import com.codeyzer.pass.sunucu.servis.api.KullaniciServis;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HariciSifreServisImpl implements HariciSifreServis {

    private final HariciSifreMapper hariciSifreMapper;
    private final KullaniciHavuzu kullaniciHavuzu;
    private final HariciSifreHavuzu hariciSifreHavuzu;
    private final KullaniciServis kullaniciServis;

    public HariciSifreServisImpl(HariciSifreMapper hariciSifreMapper, KullaniciHavuzu kullaniciHavuzu, HariciSifreHavuzu hariciSifreHavuzu, KullaniciServis kullaniciServis) {
        this.hariciSifreMapper = hariciSifreMapper;
        this.kullaniciHavuzu = kullaniciHavuzu;
        this.hariciSifreHavuzu = hariciSifreHavuzu;
        this.kullaniciServis = kullaniciServis;
    }

    @Override
    @Transactional
    public List<HariciSifreDTO> hariciSifreGetir(HariciSifreGetirDTO hariciSifreGetirDTO) {
        List<HariciSifre> hariciSifreListesi = hariciSifreHavuzu.kullaniciIleGetir(kullaniciHavuzu.getById(hariciSifreGetirDTO.getKullaniciKimlik()));
        return hariciSifreListesi.stream()
                .map(hariciSifreMapper::dtoyaDonustur)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void hariciSifreKaydet(HariciSifreKaydetDTO hariciSifreKaydetDTO) {
        HariciSifre hariciSifre = hariciSifreMapper.varligaDonustur(hariciSifreKaydetDTO);
        hariciSifre.setKullanici(kullaniciHavuzu.getById(hariciSifreKaydetDTO.getKullaniciKimlik()));
        hariciSifreHavuzu.save(hariciSifre);
    }

    @Override
    @Transactional
    public void hariciSifreSil(HariciSifreSilDTO hariciSifreSilDTO) {
        HariciSifre hariciSifre = hariciSifreHavuzu.kimlikVeKullaniyaGoreGetir(hariciSifreSilDTO.getKimlik(), kullaniciHavuzu.getById(hariciSifreSilDTO.getKullaniciKimlik()))
                .orElseThrow(() -> new CodeyzerIstisna("http.hariciSifre.hata.bulunamadi"));
       hariciSifreHavuzu.delete(hariciSifre);
    }

    @Override
    @Transactional
    public void hariciSifreYenile(HariciSifreYenileDTO hariciSifreYenileDTO) {
        kullaniciHavuzu.deleteById(hariciSifreYenileDTO.getKullaniciKimlik());
        kullaniciServis.kullaniciOlustur(KullaniciOlusturDTO.builder()
                .kimlik(hariciSifreYenileDTO.getYeniKullaniciKimlik())
                .build());
        for (HariciSifreYenileElemanDTO eleman : hariciSifreYenileDTO.getHariciSifreListesi()) {
            HariciSifre hariciSifre = hariciSifreMapper.varligaDonustur(eleman);
            hariciSifre.setKullanici(kullaniciHavuzu.getById(hariciSifreYenileDTO.getYeniKullaniciKimlik()));
            hariciSifreHavuzu.save(hariciSifre);
        }
    }
}
