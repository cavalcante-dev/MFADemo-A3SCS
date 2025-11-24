package com.a3seguranca.mfademo.controller;

import com.a3seguranca.mfademo.dto.LoginRequest;
import com.a3seguranca.mfademo.dto.LoginResponse;
import com.a3seguranca.mfademo.dto.MfaVerifyRequest;
import com.a3seguranca.mfademo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            AuthService.LoginResult result = authService.login(
                request.username(), 
                request.password(), 
                httpRequest
            );
            
            if (result.isMfaRequired()) {
                return ResponseEntity.ok(LoginResponse.withMfa(result.getTmpToken(), result.getRole()));
            } else {
                return ResponseEntity.ok(LoginResponse.withToken(result.getToken(), result.getRole()));
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciais inválidas");
            error.put("message", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }
    
    @PostMapping("/mfa/verify")
    public ResponseEntity<?> verifyMfa(@Valid @RequestBody MfaVerifyRequest request, HttpServletRequest httpRequest) {
        try {
            String token = authService.verifyMfa(request.tmpToken(), request.code(), httpRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "MFA verificado com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Código MFA inválido");
            error.put("message", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }
}
