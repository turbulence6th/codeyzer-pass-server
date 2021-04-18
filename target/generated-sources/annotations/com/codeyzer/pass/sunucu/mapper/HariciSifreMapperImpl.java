package com.codeyzer.pass.sunucu.mapper;

import com.codeyzer.pass.sunucu.dto.HariciSifreDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreGuncelleDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreKaydetDTO;
import com.codeyzer.pass.sunucu.entity.HariciSifre;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-04-10T20:58:43+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class HariciSifreMapperImpl implements HariciSifreMapper {

    @Override
    public HariciSifre varligaDonustur(HariciSifreKaydetDTO dto) {
        if ( dto == null ) {
            return null;
        }

        HariciSifre hariciSifre = new HariciSifre();

        hariciSifre.setKimlik( dto.getKimlik() );
        hariciSifre.setPlatform( dto.getPlatform() );
        hariciSifre.setKullaniciAdi( dto.getKullaniciAdi() );
        hariciSifre.setSifre( dto.getSifre() );

        return hariciSifre;
    }

    @Override
    public void varligiGuncelle(HariciSifreGuncelleDTO dto, HariciSifre varlik) {
        if ( dto == null ) {
            return;
        }

        varlik.setPlatform( dto.getPlatform() );
        varlik.setKullaniciAdi( dto.getKullaniciAdi() );
        varlik.setSifre( dto.getSifre() );
    }

    @Override
    public HariciSifreDTO dtoyaDonustur(HariciSifre varlik) {
        if ( varlik == null ) {
            return null;
        }

        HariciSifreDTO hariciSifreDTO = new HariciSifreDTO();

        hariciSifreDTO.setKimlik( varlik.getKimlik() );
        hariciSifreDTO.setPlatform( varlik.getPlatform() );
        hariciSifreDTO.setKullaniciAdi( varlik.getKullaniciAdi() );
        hariciSifreDTO.setSifre( varlik.getSifre() );

        return hariciSifreDTO;
    }
}
