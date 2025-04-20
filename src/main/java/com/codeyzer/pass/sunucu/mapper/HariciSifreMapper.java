package com.codeyzer.pass.sunucu.mapper;

import com.codeyzer.pass.sunucu.dto.HariciSifreDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreSaveRequestDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreUpdateRequestDTO;
import com.codeyzer.pass.sunucu.entity.HariciSifre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HariciSifreMapper {

    HariciSifreDTO toDTO(HariciSifre entity);

    @Mapping(target = "kullanici", ignore = true)
    HariciSifre toEntity(HariciSifreSaveRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kullanici", ignore = true)
    void updateEntityFromDto(HariciSifreUpdateRequestDTO dto, @MappingTarget HariciSifre entity);
}
