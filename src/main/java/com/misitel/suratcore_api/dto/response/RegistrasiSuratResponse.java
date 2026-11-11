package com.misitel.suratcore_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.misitel.suratcore_api.entity.RegistrasiSurat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrasiSuratResponse {

    private Long id;

    @JsonProperty("nomor_surat")
    private String nomorSurat;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_nama")
    private String userNama;

    @JsonProperty("divisi_id")
    private Long divisiId;

    @JsonProperty("nama_divisi")
    private String namaDivisi;

    private String keterangan;

    private LocalDate tanggal;

    private String status;

    public static RegistrasiSuratResponse fromEntity(RegistrasiSurat registrasiSurat) {
        return RegistrasiSuratResponse.builder()
                .id(registrasiSurat.getId())
                .nomorSurat(registrasiSurat.getNomorSurat())
                .userId(registrasiSurat.getUser() != null ? registrasiSurat.getUser().getId() : null)
                .userNama(registrasiSurat.getUser() != null ? registrasiSurat.getUser().getNama() : null)
                .divisiId(registrasiSurat.getDivision() != null ? registrasiSurat.getDivision().getId() : null)
                .namaDivisi(registrasiSurat.getDivision() != null ? registrasiSurat.getDivision().getNamaDivisi() : null)
                .keterangan(registrasiSurat.getKeterangan())
                .tanggal(registrasiSurat.getTanggal())
                .status(registrasiSurat.getStatus())
                .build();
    }
}
