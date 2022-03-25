package com.codeyzer.pass.sunucu.mapper;

import com.codeyzer.pass.sunucu.dto.HariciDosyaDTO;
import com.codeyzer.pass.sunucu.dto.HariciDosyaKaydetDTO;
import com.codeyzer.pass.sunucu.entity.HariciDosya;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HariciDosyaMapper {

    @Mapping(target = "kimlik", ignore = true)
    @Mapping(target = "kullanici", ignore = true)
    HariciDosya varligaDonustur(HariciDosyaKaydetDTO dto);

    HariciDosyaDTO dtoyaDonustur(HariciDosya varlik);
}
