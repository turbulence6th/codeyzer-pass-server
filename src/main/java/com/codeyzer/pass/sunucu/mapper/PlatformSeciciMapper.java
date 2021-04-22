package com.codeyzer.pass.sunucu.mapper;

import com.codeyzer.pass.sunucu.dto.PlatformSeciciDTO;
import com.codeyzer.pass.sunucu.entity.PlatformSecici;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlatformSeciciMapper {

    PlatformSeciciDTO dtoyaDonustur(PlatformSecici platformSecici);
}
