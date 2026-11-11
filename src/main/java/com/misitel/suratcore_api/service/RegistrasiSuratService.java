package com.misitel.suratcore_api.service;

import com.misitel.suratcore_api.dto.request.RegistrasiSuratRequest;
import com.misitel.suratcore_api.dto.response.RegistrasiSuratResponse;
import com.misitel.suratcore_api.entity.Division;
import com.misitel.suratcore_api.entity.RegistrasiSurat;
import com.misitel.suratcore_api.entity.User;
import com.misitel.suratcore_api.exception.ResourceNotFoundException;
import com.misitel.suratcore_api.repository.DivisionRepository;
import com.misitel.suratcore_api.repository.RegistrasiSuratRepository;
import com.misitel.suratcore_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrasiSuratService {

    private final RegistrasiSuratRepository registrasiSuratRepository;
    private final UserRepository userRepository;
    private final DivisionRepository divisionRepository;

    @Transactional
    public RegistrasiSuratResponse create(RegistrasiSuratRequest request) {
        String noSurat = request.getNomorSurat().trim();
        if (registrasiSuratRepository.existsByNomorSurat(noSurat)) {
            throw new IllegalArgumentException("Nomor surat '" + noSurat + "' sudah terdaftar");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + request.getUserId() + " tidak ditemukan"));

        Division division = divisionRepository.findById(request.getDivisiId())
                .orElseThrow(() -> new ResourceNotFoundException("Divisi dengan ID " + request.getDivisiId() + " tidak ditemukan"));

        RegistrasiSurat registrasiSurat = RegistrasiSurat.builder()
                .nomorSurat(noSurat)
                .user(user)
                .division(division)
                .keterangan(request.getKeterangan())
                .tanggal(request.getTanggal())
                .status(request.getStatus().trim())
                .build();

        RegistrasiSurat saved = registrasiSuratRepository.save(registrasiSurat);
        return RegistrasiSuratResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<RegistrasiSuratResponse> getAll(String status, Long divisiId) {
        List<RegistrasiSurat> list;
        if (status != null && !status.trim().isEmpty()) {
            list = registrasiSuratRepository.findByStatus(status.trim());
        } else if (divisiId != null) {
            list = registrasiSuratRepository.findByDivisionId(divisiId);
        } else {
            list = registrasiSuratRepository.findAll();
        }

        return list.stream()
                .map(RegistrasiSuratResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RegistrasiSuratResponse getById(Long id) {
        RegistrasiSurat registrasiSurat = registrasiSuratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi surat dengan ID " + id + " tidak ditemukan"));
        return RegistrasiSuratResponse.fromEntity(registrasiSurat);
    }

    @Transactional
    public RegistrasiSuratResponse update(Long id, RegistrasiSuratRequest request) {
        RegistrasiSurat registrasiSurat = registrasiSuratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi surat dengan ID " + id + " tidak ditemukan"));

        String newNomor = request.getNomorSurat().trim();
        if (!registrasiSurat.getNomorSurat().equalsIgnoreCase(newNomor)
                && registrasiSuratRepository.existsByNomorSurat(newNomor)) {
            throw new IllegalArgumentException("Nomor surat '" + newNomor + "' sudah terdaftar");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + request.getUserId() + " tidak ditemukan"));

        Division division = divisionRepository.findById(request.getDivisiId())
                .orElseThrow(() -> new ResourceNotFoundException("Divisi dengan ID " + request.getDivisiId() + " tidak ditemukan"));

        registrasiSurat.setNomorSurat(newNomor);
        registrasiSurat.setUser(user);
        registrasiSurat.setDivision(division);
        registrasiSurat.setKeterangan(request.getKeterangan());
        registrasiSurat.setTanggal(request.getTanggal());
        registrasiSurat.setStatus(request.getStatus().trim());

        RegistrasiSurat updated = registrasiSuratRepository.save(registrasiSurat);
        return RegistrasiSuratResponse.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        RegistrasiSurat registrasiSurat = registrasiSuratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registrasi surat dengan ID " + id + " tidak ditemukan"));
        registrasiSuratRepository.delete(registrasiSurat);
    }
}

