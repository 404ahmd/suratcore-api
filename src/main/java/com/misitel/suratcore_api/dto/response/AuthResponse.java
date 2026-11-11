package com.misitel.suratcore_api.dto.response;

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
public class AuthResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long id;
    private String username;
    private String nama;
    private String role;
}

