package com.codeyzer.pass.sunucu.controller;

import com.codeyzer.pass.sunucu.core.Cevap;
import com.codeyzer.pass.sunucu.dto.PlatformSeciciDTO;
import com.codeyzer.pass.sunucu.dto.PlatformSeciciGetirDTO;
import com.codeyzer.pass.sunucu.servis.PlatformSeciciServis;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/platform_secici")
public class PlatformSeciciController {

    private final PlatformSeciciServis platformSeciciServis;

    public PlatformSeciciController(PlatformSeciciServis platformSeciciServis) {
        this.platformSeciciServis = platformSeciciServis;
    }

    @RequestMapping(path = "/getir", method = RequestMethod.POST)
    public Cevap<PlatformSeciciDTO> getir(@RequestBody PlatformSeciciGetirDTO istek) {
        PlatformSeciciDTO platformSeciciDTO = platformSeciciServis.getir(istek);
        return new Cevap<>(platformSeciciDTO, "Platform seçici getirildi.");
    }
}
