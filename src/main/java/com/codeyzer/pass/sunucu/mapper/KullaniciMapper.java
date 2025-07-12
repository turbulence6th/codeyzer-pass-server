package com.codeyzer.pass.sunucu.mapper;

import com.codeyzer.pass.sunucu.dto.KullaniciOlusturRequestDTO;
import com.codeyzer.pass.sunucu.dto.SifreGuncelleRequestDTO;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KullaniciMapper {

    @Mapping(target = "sifreHash", ignore = true)
    Kullanici toEntity(KullaniciOlusturRequestDTO request);

    @Mapping(target = "kullaniciKimlik", source = "yeniKullaniciKimlik")
    @Mapping(target = "sifreSha512", source = "yeniSifreSha512")
    KullaniciOlusturRequestDTO toKullaniciOlusturRequestDTO(SifreGuncelleRequestDTO request);
}
