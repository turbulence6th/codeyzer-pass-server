package com.codeyzer.pass.sunucu.servis;

import com.codeyzer.pass.sunucu.dto.*;
import com.codeyzer.pass.sunucu.entity.HariciSifre;
import com.codeyzer.pass.sunucu.exception.CodeyzerIstisna;
import com.codeyzer.pass.sunucu.mapper.HariciSifreMapper;
import com.codeyzer.pass.sunucu.repository.HariciSifreHavuzu;
import com.codeyzer.pass.sunucu.repository.KullaniciHavuzu;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HariciSifreServis {

    private final HariciSifreMapper hariciSifreMapper;
    private final KullaniciHavuzu kullaniciHavuzu;
    private final HariciSifreHavuzu hariciSifreHavuzu;

    public HariciSifreServis(HariciSifreMapper hariciSifreMapper, KullaniciHavuzu kullaniciHavuzu, HariciSifreHavuzu hariciSifreHavuzu) {
        this.hariciSifreMapper = hariciSifreMapper;
        this.kullaniciHavuzu = kullaniciHavuzu;
        this.hariciSifreHavuzu = hariciSifreHavuzu;
    }

    @Transactional
    public List<HariciSifreDTO> hariciSifreGetir(HariciSifreGetirDTO hariciSifreGetirDTO) {
        List<HariciSifre> hariciSifreListesi = hariciSifreHavuzu.platformVeKullaniciyaGoreGetir(hariciSifreGetirDTO.getPlatform(), kullaniciHavuzu.getOne(hariciSifreGetirDTO.getKullaniciKimlik()));
        return hariciSifreListesi.stream()
                .map(hariciSifreMapper::dtoyaDonustur)
                .collect(Collectors.toList());
    }

    @Transactional
    public void hariciSifreKaydet(HariciSifreKaydetDTO hariciSifreKaydetDTO) {
        HariciSifre hariciSifre = hariciSifreMapper.varligaDonustur(hariciSifreKaydetDTO);
        hariciSifre.setKullanici(kullaniciHavuzu.getOne(hariciSifreKaydetDTO.getKullaniciKimlik()));
        hariciSifreHavuzu.save(hariciSifre);
    }

    @Transactional
    public void hariciSifreSil(HariciSifreSilDTO hariciSifreSilDTO) {
        HariciSifre hariciSifre = hariciSifreHavuzu.kimlikVeKullaniyaGoreGetir(hariciSifreSilDTO.getKimlik(), kullaniciHavuzu.getOne(hariciSifreSilDTO.getKullaniciKimlik()))
                .orElseThrow(() -> new CodeyzerIstisna("Harici şifre bulunamadı."));
       hariciSifreHavuzu.delete(hariciSifre);
    }
}
