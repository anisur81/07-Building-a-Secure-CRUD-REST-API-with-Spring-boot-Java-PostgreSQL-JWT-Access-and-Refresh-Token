package com.example.restapi.controller;

import com.example.restapi.dto.AuthResponse;
import com.example.restapi.dto.LoginRequest;
import com.example.restapi.dto.RegisterRequest;
import com.example.restapi.entity.User;
import com.example.restapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		System.out.println("==== LOGIN CONTROLLER CALLED ====");
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
	

}