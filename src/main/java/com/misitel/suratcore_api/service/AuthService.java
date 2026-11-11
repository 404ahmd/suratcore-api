package com.misitel.suratcore_api.service;

import com.misitel.suratcore_api.dto.request.LoginRequest;
import com.misitel.suratcore_api.dto.request.RegisterRequest;
import com.misitel.suratcore_api.dto.response.AuthResponse;
import com.misitel.suratcore_api.entity.Division;
import com.misitel.suratcore_api.entity.User;
import com.misitel.suratcore_api.repository.DivisionRepository;
import com.misitel.suratcore_api.repository.UserRepository;
import com.misitel.suratcore_api.security.CustomUserDetails;
import com.misitel.suratcore_api.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DivisionRepository divisionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .nama(user.getNama())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username sudah terdaftar");
        }

        Division division = divisionRepository.findById(request.getDivisiId())
                .orElseThrow(() -> new IllegalArgumentException("Divisi dengan ID " + request.getDivisiId() + " tidak ditemukan"));

        User user = User.builder()
                .nama(request.getNama())
                .division(division)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .nama(user.getNama())
                .role(user.getRole())
                .build();
    }
}
