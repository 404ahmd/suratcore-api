package com.misitel.suratcore_api.controller;

import com.misitel.suratcore_api.dto.request.DivisionRequest;
import com.misitel.suratcore_api.dto.response.DivisionResponse;
import com.misitel.suratcore_api.service.DivisionService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/divisions")
@RequiredArgsConstructor
public class DivisionController {

    private final DivisionService divisionService;

    @PostMapping
    public ResponseEntity<DivisionResponse> createDivision(@Valid @RequestBody DivisionRequest request) {
        DivisionResponse response = divisionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DivisionResponse>> getAllDivisions() {
        List<DivisionResponse> divisions = divisionService.getAll();
        return ResponseEntity.ok(divisions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DivisionResponse> getDivisionById(@PathVariable Long id) {
        DivisionResponse response = divisionService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DivisionResponse> updateDivision(
            @PathVariable Long id,
            @Valid @RequestBody DivisionRequest request
    ) {
        DivisionResponse response = divisionService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDivision(@PathVariable Long id) {
        divisionService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Divisi berhasil dihapus");
        return ResponseEntity.ok(response);
    }
}

