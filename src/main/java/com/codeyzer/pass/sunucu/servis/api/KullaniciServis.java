package com.codeyzer.pass.sunucu.servis.api;

import com.codeyzer.pass.sunucu.dto.KullaniciDTO;
import com.codeyzer.pass.sunucu.dto.KullaniciDogrulaDTO;
import com.codeyzer.pass.sunucu.dto.KullaniciOlusturDTO;

public interface KullaniciServis {

    KullaniciDTO kullaniciOlustur(KullaniciOlusturDTO kullaniciOlusturDTO);
    void kullaniciDogrula(KullaniciDogrulaDTO kullaniciDogrulaDTO);
}
