package com.misitel.suratcore_api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RegistrasiSuratRequest {

    @NotBlank(message = "Nomor surat wajib diisi")
    @JsonAlias({"nomor_surat", "nomorSurat"})
    private String nomorSurat;

    @NotNull(message = "User ID wajib diisi")
    @JsonAlias({"user_id", "userId"})
    private Long userId;

    @NotNull(message = "Divisi ID wajib diisi")
    @JsonAlias({"divisi_id", "divisiId"})
    private Long divisiId;

    private String keterangan;

    @NotNull(message = "Tanggal wajib diisi")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggal;

    @NotBlank(message = "Status wajib diisi")
    private String status;
}

