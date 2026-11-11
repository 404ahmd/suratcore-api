package com.misitel.suratcore_api.controller;

import com.misitel.suratcore_api.dto.request.RegistrasiSuratRequest;
import com.misitel.suratcore_api.dto.response.RegistrasiSuratResponse;
import com.misitel.suratcore_api.service.RegistrasiSuratService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrasi-surat")
@RequiredArgsConstructor
public class RegistrasiSuratController {

    private final RegistrasiSuratService registrasiSuratService;

    @PostMapping
    public ResponseEntity<RegistrasiSuratResponse> createRegistrasiSurat(
            @Valid @RequestBody RegistrasiSuratRequest request
    ) {
        RegistrasiSuratResponse response = registrasiSuratService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RegistrasiSuratResponse>> getAllRegistrasiSurat(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long divisiId
    ) {
        List<RegistrasiSuratResponse> list = registrasiSuratService.getAll(status, divisiId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrasiSuratResponse> getRegistrasiSuratById(@PathVariable Long id) {
        RegistrasiSuratResponse response = registrasiSuratService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistrasiSuratResponse> updateRegistrasiSurat(
            @PathVariable Long id,
            @Valid @RequestBody RegistrasiSuratRequest request
    ) {
        RegistrasiSuratResponse response = registrasiSuratService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRegistrasiSurat(@PathVariable Long id) {
        registrasiSuratService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registrasi surat berhasil dihapus");
        return ResponseEntity.ok(response);
    }
}

