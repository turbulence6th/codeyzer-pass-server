package com.codeyzer.pass.sunucu.mapper;

import com.codeyzer.pass.sunucu.dto.KullaniciDTO;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KullaniciMapper {
    KullaniciDTO dtoyaDonustur(Kullanici kullanici);
}
