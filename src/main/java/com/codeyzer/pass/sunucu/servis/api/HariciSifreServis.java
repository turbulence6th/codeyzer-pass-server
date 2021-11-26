package com.codeyzer.pass.sunucu.servis.api;

import com.codeyzer.pass.sunucu.dto.*;

import java.util.List;

public interface HariciSifreServis {

    List<HariciSifreDTO> hariciSifreGetir(HariciSifreGetirDTO hariciSifreGetirDTO);
    void hariciSifreKaydet(HariciSifreKaydetDTO hariciSifreKaydetDTO);
    void hariciSifreGuncelle(HariciSifreGuncelleDTO hariciSifreGuncelleDTO);
    void hariciSifreSil(HariciSifreSilDTO hariciSifreSilDTO);
    void hariciSifreYenile(HariciSifreYenileDTO hariciSifreYenileDTO);
}
