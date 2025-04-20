package com.codeyzer.pass.sunucu.service;

import com.codeyzer.pass.sunucu.dto.HariciSifreDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreSaveRequestDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreUpdateRequestDTO;
import com.codeyzer.pass.sunucu.entity.HariciSifre;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import com.codeyzer.pass.sunucu.exception.CodeyzerPassException;
import com.codeyzer.pass.sunucu.mapper.HariciSifreMapper;
import com.codeyzer.pass.sunucu.repository.HariciSifreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HariciSifreService {

    private final HariciSifreRepository repository;
    private final HariciSifreMapper mapper;
    private final KullaniciService kullaniciService;

    @Transactional(readOnly = true)
    public List<HariciSifreDTO> getAllForCurrentUser() {
        Kullanici kullanici = kullaniciService.getCurrentUser();
        return repository.findAllByKullanici_KullaniciKimlik(kullanici.getKullaniciKimlik())
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional
    public HariciSifreDTO save(HariciSifreSaveRequestDTO dto) {
        HariciSifre entity = mapper.toEntity(dto);
        entity.setKullanici(kullaniciService.getCurrentUser());
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public HariciSifreDTO update(String id, HariciSifreUpdateRequestDTO dto) {
        HariciSifre existing = repository.findById(id).orElseThrow(() -> new CodeyzerPassException(HttpStatus.NOT_FOUND, "Harici şifre bulunamadı."));
        if (!existing.getKullanici().getKullaniciKimlik().equals(kullaniciService.getCurrentUser().getKullaniciKimlik())) {
            throw new CodeyzerPassException(HttpStatus.NOT_FOUND, "Harici şifre bulunamadı.");
        }
        mapper.updateEntityFromDto(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }

    @Transactional
    public void delete(String id) {
        HariciSifre existing = repository.findById(id).orElseThrow(() -> new CodeyzerPassException(HttpStatus.NOT_FOUND, "Harici şifre bulunamadı."));
        if (!existing.getKullanici().equals(kullaniciService.getCurrentUser())) {
            throw new CodeyzerPassException(HttpStatus.NOT_FOUND, "Harici şifre bulunamadı.");
        }
        repository.delete(existing);
    }
}
