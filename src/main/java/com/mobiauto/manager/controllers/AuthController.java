package com.mobiauto.manager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobiauto.manager.dto.AuthRequest;
import com.mobiauto.manager.dto.AuthResponse;
import com.mobiauto.manager.dto.RegisterRequest;
import com.mobiauto.manager.models.Usuario;
import com.mobiauto.manager.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequest request,
            Authentication authentication) {
        
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(authService.register(request, usuarioLogado));
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}

