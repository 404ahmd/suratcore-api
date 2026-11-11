package com.misitel.suratcore_api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DivisionRequest {

    @NotBlank(message = "Kode divisi wajib diisi")
    @JsonAlias({"kode_divisi", "kodeDivisi"})
    private String kodeDivisi;

    @NotBlank(message = "Nama divisi wajib diisi")
    @JsonAlias({"nama_divisi", "namaDivisi"})
    private String namaDivisi;
}
