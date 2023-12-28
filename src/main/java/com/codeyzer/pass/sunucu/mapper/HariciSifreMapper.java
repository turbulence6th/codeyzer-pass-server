package com.codeyzer.pass.sunucu.mapper;

import com.codeyzer.pass.sunucu.dto.HariciSifreDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreGuncelleDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreKaydetDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreYenileElemanDTO;
import com.codeyzer.pass.sunucu.entity.HariciSifre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HariciSifreMapper {

    @Mapping(target = "kullanici", ignore = true)
    HariciSifre varligaDonustur(HariciSifreKaydetDTO dto);

    @Mapping(target = "kimlik", ignore = true)
    @Mapping(target = "kullanici", ignore = true)
    void varligiGuncelle(HariciSifreGuncelleDTO dto, @MappingTarget HariciSifre varlik);

    HariciSifreDTO dtoyaDonustur(HariciSifre varlik);

    @Mapping(target = "kullanici", ignore = true)
    @Mapping(target = "kimlik", ignore = true)
    HariciSifre varligaDonustur(HariciSifreYenileElemanDTO dto);
}
