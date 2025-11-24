package com.a3seguranca.mfademo.controller;

import com.a3seguranca.mfademo.entity.AccessLog;
import com.a3seguranca.mfademo.repository.AccessLogRepository;
import com.a3seguranca.mfademo.security.JWTUtil;
import com.a3seguranca.mfademo.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LogController {
    
    @Autowired
    private AccessLogRepository accessLogRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @GetMapping("/logs")
    public ResponseEntity<?> getAllLogs(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(authHeader);
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }
            
            // Apenas ADMIN pode ver todos os logs
            String role = jwtUtil.extractRole(authHeader.substring(7));
            if (!"ADMIN".equals(role)) {
                auditService.log(username, "/api/logs", false, 
                    "Tentativa de acesso negada - não é ADMIN", request);
                return ResponseEntity.status(403).body(Map.of("error", "Acesso negado"));
            }
            
            List<AccessLog> logs = accessLogRepository.findAllByOrderByTimestampDesc();
            auditService.log(username, "/api/logs", true, 
                "Logs de auditoria acessados", request);
            
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/logs/my")
    public ResponseEntity<?> getMyLogs(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(authHeader);
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }
            
            List<AccessLog> logs = accessLogRepository.findByUsernameOrderByTimestampDesc(username);
            auditService.log(username, "/api/logs/my", true, 
                "Próprios logs acessados", request);
            
            return ResponseEntity.ok(logs);
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
