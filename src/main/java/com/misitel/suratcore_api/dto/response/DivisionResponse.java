package com.misitel.suratcore_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.misitel.suratcore_api.entity.Division;
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
public class DivisionResponse {

    private Long id;

    @JsonProperty("kode_divisi")
    private String kodeDivisi;

    @JsonProperty("nama_divisi")
    private String namaDivisi;

    public static DivisionResponse fromEntity(Division division) {
        return DivisionResponse.builder()
                .id(division.getId())
                .kodeDivisi(division.getKodeDivisi())
                .namaDivisi(division.getNamaDivisi())
                .build();
    }
}
