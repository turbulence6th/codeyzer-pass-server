package com.codeyzer.pass.sunucu.controller;

import com.codeyzer.pass.sunucu.dto.HariciSifreDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreSaveRequestDTO;
import com.codeyzer.pass.sunucu.dto.HariciSifreUpdateRequestDTO;
import com.codeyzer.pass.sunucu.service.HariciSifreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harici-sifre")
@RequiredArgsConstructor
@Slf4j
public class HariciSifreController {

    private final HariciSifreService service;

    @GetMapping
    public List<HariciSifreDTO> getAll() {
        return service.getAllForCurrentUser();
    }

    @PostMapping("/kaydet")
    public HariciSifreDTO save(@RequestBody HariciSifreSaveRequestDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/guncelle/{id}")
    public HariciSifreDTO update(@PathVariable String id, @RequestBody HariciSifreUpdateRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/sil/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
