package com.misitel.suratcore_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RegisterRequest {

    @NotBlank(message = "Nama wajib diisi")
    private String nama;

    @NotNull(message = "Divisi ID wajib diisi")
    private Long divisiId;

    @NotBlank(message = "Username wajib diisi")
    private String username;

    @NotBlank(message = "Password wajib diisi")
    private String password;

    @NotBlank(message = "Role wajib diisi")
    private String role;
}

