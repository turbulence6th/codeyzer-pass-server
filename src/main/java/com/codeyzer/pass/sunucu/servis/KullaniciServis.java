package com.codeyzer.pass.sunucu.servis;

import com.codeyzer.pass.sunucu.dto.KullaniciDogrulaDTO;
import com.codeyzer.pass.sunucu.dto.KullaniciOlusturDTO;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import com.codeyzer.pass.sunucu.exception.CodeyzerIstisna;
import com.codeyzer.pass.sunucu.repository.KullaniciHavuzu;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class KullaniciServis {

    private final KullaniciHavuzu kullaniciHavuzu;

    public KullaniciServis(KullaniciHavuzu kullaniciHavuzu) {
        this.kullaniciHavuzu = kullaniciHavuzu;
    }

    @Transactional
    public void kullaniciOlustur(KullaniciOlusturDTO kullaniciOlusturDTO) {
        boolean kimlikIleKullaniciVarMi = kullaniciHavuzu.kimlikIleGetir(kullaniciOlusturDTO.getKimlik()).isPresent();
        if (kimlikIleKullaniciVarMi) {
            throw new CodeyzerIstisna("Bu kullanıcı zaten tanımlıdır.", kullaniciOlusturDTO.getKimlik());
        }

        Kullanici kullanici = Kullanici.builder()
                .kimlik(kullaniciOlusturDTO.getKimlik())
                .build();

        kullaniciHavuzu.save(kullanici);
    }

    public void kullaniciDogrula(KullaniciDogrulaDTO kullaniciDogrulaDTO) {
        boolean kimlikIleKullaniciVarMi = kullaniciHavuzu.kimlikIleGetir(kullaniciDogrulaDTO.getKimlik()).isPresent();
        if (!kimlikIleKullaniciVarMi) {
            throw new CodeyzerIstisna("Kullanıcı bulunamadı", kullaniciDogrulaDTO.getKimlik());
        }
    }
}
