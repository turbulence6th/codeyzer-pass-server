package com.codeyzer.pass.sunucu.servis;

import com.codeyzer.pass.sunucu.dto.PlatformSeciciDTO;
import com.codeyzer.pass.sunucu.dto.PlatformSeciciGetirDTO;
import com.codeyzer.pass.sunucu.entity.PlatformSecici;
import com.codeyzer.pass.sunucu.exception.CodeyzerIstisna;
import com.codeyzer.pass.sunucu.mapper.PlatformSeciciMapper;
import com.codeyzer.pass.sunucu.repository.PlatformSeciciHavuzu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Service
public class PlatformSeciciServis {

    private final PlatformSeciciHavuzu platformSeciciHavuzu;
    private final PlatformSeciciMapper platformSeciciMapper;

    @Autowired
    public PlatformSeciciServis(PlatformSeciciHavuzu platformSeciciHavuzu, PlatformSeciciMapper platformSeciciMapper) {
        this.platformSeciciHavuzu = platformSeciciHavuzu;
        this.platformSeciciMapper = platformSeciciMapper;
    }

    @Transactional
    public PlatformSeciciDTO getir(PlatformSeciciGetirDTO platformSeciciGetirDTO) {
        PlatformSecici platformSecici = platformSeciciHavuzu.hepsiniGetir()
                .filter(x -> Pattern.matches(x.getPlatformRegex(), platformSeciciGetirDTO.getSorgu()))
                .findAny()
                .orElseThrow(() -> new CodeyzerIstisna("Platform seçici bulunamadı"));
        return platformSeciciMapper.dtoyaDonustur(platformSecici);
    }
}
