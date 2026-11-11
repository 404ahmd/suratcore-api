package com.misitel.suratcore_api.service;

import com.misitel.suratcore_api.dto.request.DivisionRequest;
import com.misitel.suratcore_api.dto.response.DivisionResponse;
import com.misitel.suratcore_api.entity.Division;
import com.misitel.suratcore_api.exception.ResourceNotFoundException;
import com.misitel.suratcore_api.repository.DivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DivisionService {

    private final DivisionRepository divisionRepository;

    @Transactional
    public DivisionResponse create(DivisionRequest request) {
        if (divisionRepository.existsByKodeDivisi(request.getKodeDivisi())) {
            throw new IllegalArgumentException("Kode divisi '" + request.getKodeDivisi() + "' sudah digunakan");
        }

        Division division = Division.builder()
                .kodeDivisi(request.getKodeDivisi().trim())
                .namaDivisi(request.getNamaDivisi().trim())
                .build();

        Division saved = divisionRepository.save(division);
        return DivisionResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<DivisionResponse> getAll() {
        return divisionRepository.findAll().stream()
                .map(DivisionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DivisionResponse getById(Long id) {
        Division division = divisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Divisi dengan ID " + id + " tidak ditemukan"));
        return DivisionResponse.fromEntity(division);
    }

    @Transactional
    public DivisionResponse update(Long id, DivisionRequest request) {
        Division division = divisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Divisi dengan ID " + id + " tidak ditemukan"));

        String newKode = request.getKodeDivisi().trim();
        if (!division.getKodeDivisi().equalsIgnoreCase(newKode) && divisionRepository.existsByKodeDivisi(newKode)) {
            throw new IllegalArgumentException("Kode divisi '" + newKode + "' sudah digunakan");
        }

        division.setKodeDivisi(newKode);
        division.setNamaDivisi(request.getNamaDivisi().trim());

        Division updated = divisionRepository.save(division);
        return DivisionResponse.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        Division division = divisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Divisi dengan ID " + id + " tidak ditemukan"));
        divisionRepository.delete(division);
    }
}
