package com.codeyzer.pass.sunucu.servis.api;

import com.codeyzer.pass.sunucu.dto.HariciDosyaDTO;
import com.codeyzer.pass.sunucu.dto.HariciDosyaGetirDTO;
import com.codeyzer.pass.sunucu.dto.HariciDosyaIndirDTO;
import com.codeyzer.pass.sunucu.dto.HariciDosyaKaydetDTO;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface HariciDosyaServis {

    String dosyaYukle(InputStream is);
    void dosyaIndir(HariciDosyaIndirDTO hariciDosyaIndirDTO, OutputStream os);
    List<HariciDosyaDTO> hariciDosyaGetir(HariciDosyaGetirDTO hariciDosyaGetirDTO);
    void hariciDosyaKaydet(HariciDosyaKaydetDTO hariciDosyaKaydetDTO);
}
