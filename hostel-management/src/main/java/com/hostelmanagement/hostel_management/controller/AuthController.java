
package com.hostelmanagement.hostel_management.controller;

import com.hostelmanagement.hostel_management.dto.LoginDTO;
import com.hostelmanagement.hostel_management.dto.RegisterDTO;
import com.hostelmanagement.hostel_management.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterDTO dto) {

        String message = authService.register(dto);

        return ResponseEntity.ok(
                Map.of("message", message)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginDTO dto) {

        Map<String, Object> response =
                authService.login(dto);

        return ResponseEntity.ok(response);
    }
}