package com.a3seguranca.mfademo.controller;

import com.a3seguranca.mfademo.dto.UserDTO;
import com.a3seguranca.mfademo.repository.UserRepository;
import com.a3seguranca.mfademo.security.JWTUtil;
import com.a3seguranca.mfademo.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @GetMapping("/panel")
    public ResponseEntity<?> getAdminPanel(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(authHeader);
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }
            
            // Verificar se é ADMIN
            String role = jwtUtil.extractRole(authHeader.substring(7));
            if (!"ADMIN".equals(role)) {
                auditService.log(username, "/api/admin/panel", false, 
                    "Tentativa de acesso negada - usuário não é ADMIN", request);
                return ResponseEntity.status(403).body(Map.of(
                    "error", "Acesso negado",
                    "message", "Apenas administradores podem acessar este painel"
                ));
            }
            
            Map<String, Object> panel = new HashMap<>();
            panel.put("message", "Painel Administrativo");
            panel.put("username", username);
            panel.put("features", List.of(
                "Gerenciar usuários",
                "Visualizar todos os logs",
                "Configurar sistema",
                "Acessar relatórios completos"
            ));
            
            auditService.log(username, "/api/admin/panel", true, 
                "Painel admin acessado com sucesso", request);
            
            return ResponseEntity.ok(panel);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(authHeader);
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }
            
            String role = jwtUtil.extractRole(authHeader.substring(7));
            if (!"ADMIN".equals(role)) {
                auditService.log(username, "/api/admin/users", false, 
                    "Tentativa de acesso negada - não é ADMIN", request);
                return ResponseEntity.status(403).body(Map.of("error", "Acesso negado"));
            }
            
            List<UserDTO> users = userRepository.findAll().stream()
                .map(user -> new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getRole(),
                    user.isEnabled(),
                    user.isMfaEnabled(),
                    user.getEmail()
                ))
                .collect(Collectors.toList());
            
            auditService.log(username, "/api/admin/users", true, 
                "Lista de usuários acessada", request);
            
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    private String extractUsernameFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token);
        } catch (Exception e) {
            return null;
        }
    }
}
