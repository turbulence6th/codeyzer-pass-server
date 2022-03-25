package com.codeyzer.pass.sunucu.servis.impl;

import com.codeyzer.pass.sunucu.dto.HariciDosyaDTO;
import com.codeyzer.pass.sunucu.dto.HariciDosyaGetirDTO;
import com.codeyzer.pass.sunucu.dto.HariciDosyaIndirDTO;
import com.codeyzer.pass.sunucu.dto.HariciDosyaKaydetDTO;
import com.codeyzer.pass.sunucu.entity.HariciDosya;
import com.codeyzer.pass.sunucu.entity.HariciSifre;
import com.codeyzer.pass.sunucu.exception.CodeyzerIstisna;
import com.codeyzer.pass.sunucu.mapper.HariciDosyaMapper;
import com.codeyzer.pass.sunucu.repository.HariciDosyaHavuzu;
import com.codeyzer.pass.sunucu.repository.KullaniciHavuzu;
import com.codeyzer.pass.sunucu.servis.api.HariciDosyaServis;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

@Service
@RequiredArgsConstructor
public class HariciDosyaServisImpl implements HariciDosyaServis {

    private final MongoClient mongoClient;
    private final HariciDosyaMapper hariciDosyaMapper;
    private final HariciDosyaHavuzu hariciDosyaHavuzu;
    private final KullaniciHavuzu kullaniciHavuzu;

    @Override
    public String dosyaYukle(InputStream is) {
        MongoDatabase database = mongoClient.getDatabase("harici_dosya");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        ObjectId fileId = gridFSBucket.uploadFromStream(UUID.randomUUID().toString(), is);
        return fileId.toHexString();
    }

    @Override
    public void dosyaIndir(HariciDosyaIndirDTO hariciDosyaIndirDTO, OutputStream os) {
        MongoDatabase database = mongoClient.getDatabase("harici_dosya");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        gridFSBucket.downloadToStream(new ObjectId(hariciDosyaIndirDTO.getMongoKimlik()), os);
    }

    @Override
    public List<HariciDosyaDTO> hariciDosyaGetir(HariciDosyaGetirDTO hariciDosyaGetirDTO) {
        List<HariciDosya> hariciDosyaListesi = hariciDosyaHavuzu.kullaniciIleGetir(kullaniciHavuzu.getById(hariciDosyaGetirDTO.getKullaniciKimlik()));
        return hariciDosyaListesi.stream()
                .map(hariciDosyaMapper::dtoyaDonustur)
                .collect(Collectors.toList());
    }

    @Override
    public void hariciDosyaKaydet(HariciDosyaKaydetDTO hariciDosyaKaydetDTO) {
        MongoDatabase database = mongoClient.getDatabase("harici_dosya");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        GridFSFile gridFSFile = gridFSBucket.find(eq("_id", new ObjectId(hariciDosyaKaydetDTO.getMongoKimlik()))).first();
        if (gridFSFile == null) {
            throw new CodeyzerIstisna("http.hariciDosya.hata.bulunamadi");
        }

        boolean mongoKimlikIleVarMi = hariciDosyaHavuzu.mongoKimlikIleGetir(hariciDosyaKaydetDTO.getMongoKimlik()).isPresent();
        if (mongoKimlikIleVarMi) {
            throw new CodeyzerIstisna("http.hariciDosya.hata.eslenmis");
        }

        HariciDosya hariciDosya = hariciDosyaMapper.varligaDonustur(hariciDosyaKaydetDTO);
        hariciDosya.setKullanici(kullaniciHavuzu.getById(hariciDosyaKaydetDTO.getKullaniciKimlik()));
        hariciDosyaHavuzu.save(hariciDosya);
    }
}
