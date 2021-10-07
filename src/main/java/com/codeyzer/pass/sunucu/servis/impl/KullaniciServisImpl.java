package com.codeyzer.pass.sunucu.servis.impl;

import com.codeyzer.pass.sunucu.dto.KullaniciDTO;
import com.codeyzer.pass.sunucu.dto.KullaniciDogrulaDTO;
import com.codeyzer.pass.sunucu.dto.KullaniciOlusturDTO;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import com.codeyzer.pass.sunucu.exception.CodeyzerIstisna;
import com.codeyzer.pass.sunucu.mapper.KullaniciMapper;
import com.codeyzer.pass.sunucu.repository.KullaniciHavuzu;
import com.codeyzer.pass.sunucu.servis.api.KullaniciServis;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class KullaniciServisImpl implements KullaniciServis {

    private final KullaniciHavuzu kullaniciHavuzu;
    private final KullaniciMapper kullaniciMapper;

    public KullaniciServisImpl(KullaniciHavuzu kullaniciHavuzu, KullaniciMapper kullaniciMapper) {
        this.kullaniciHavuzu = kullaniciHavuzu;
        this.kullaniciMapper = kullaniciMapper;
    }

    @Override
    @Transactional
    public KullaniciDTO kullaniciOlustur(KullaniciOlusturDTO kullaniciOlusturDTO) {
        boolean kimlikIleKullaniciVarMi = kullaniciHavuzu.kimlikIleGetir(kullaniciOlusturDTO.getKimlik()).isPresent();
        if (kimlikIleKullaniciVarMi) {
            throw new CodeyzerIstisna("http.kullanici.hata.mevcut", kullaniciOlusturDTO.getKimlik());
        }

        Kullanici kullanici = Kullanici.builder()
                .kimlik(kullaniciOlusturDTO.getKimlik())
                .build();

        kullaniciHavuzu.save(kullanici);

        return kullaniciMapper.dtoyaDonustur(kullanici);
    }

    @Override
    public void kullaniciDogrula(KullaniciDogrulaDTO kullaniciDogrulaDTO) {
        boolean kimlikIleKullaniciVarMi = kullaniciHavuzu.kimlikIleGetir(kullaniciDogrulaDTO.getKimlik()).isPresent();
        if (!kimlikIleKullaniciVarMi) {
            throw new CodeyzerIstisna("http.kullanici.hata.bulunamadi", kullaniciDogrulaDTO.getKimlik());
        }
    }
}
