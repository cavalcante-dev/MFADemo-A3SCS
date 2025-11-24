package com.a3seguranca.mfademo.controller;

import com.a3seguranca.mfademo.dto.UserDTO;
import com.a3seguranca.mfademo.entity.UserEntity;
import com.a3seguranca.mfademo.repository.UserRepository;
import com.a3seguranca.mfademo.security.JWTUtil;
import com.a3seguranca.mfademo.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(authHeader);
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }
            
            Optional<UserEntity> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                auditService.log(username, "/api/user/profile", false, 
                    "Usuário não encontrado", request);
                return ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado"));
            }
            
            UserEntity user = userOpt.get();
            UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isEnabled(),
                user.isMfaEnabled(),
                user.getEmail()
            );
            
            auditService.log(username, "/api/user/profile", true, 
                "Perfil acessado com sucesso", request);
            
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(authHeader);
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }
            
            Optional<UserEntity> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado"));
            }
            
            UserEntity user = userOpt.get();
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("username", user.getUsername());
            dashboard.put("role", user.getRole());
            dashboard.put("message", "Bem-vindo ao dashboard! Você tem acesso como " + user.getRole());
            dashboard.put("permissions", getPermissionsForRole(user.getRole()));
            
            auditService.log(username, "/api/user/dashboard", true, 
                "Dashboard acessado", request);
            
            return ResponseEntity.ok(dashboard);
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
    
    private Map<String, Boolean> getPermissionsForRole(String role) {
        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("viewOwnProfile", true);
        permissions.put("viewReports", role.equals("MANAGER") || role.equals("ADMIN"));
        permissions.put("viewAdminPanel", role.equals("ADMIN"));
        permissions.put("manageUsers", role.equals("ADMIN"));
        return permissions;
    }
}
