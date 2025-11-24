package com.a3seguranca.mfademo.controller;

import com.a3seguranca.mfademo.security.JWTUtil;
import com.a3seguranca.mfademo.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @GetMapping("/reports")
    public ResponseEntity<?> getReports(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(authHeader);
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }
            
            // Verificar se é MANAGER ou ADMIN
            String role = jwtUtil.extractRole(authHeader.substring(7));
            if (!"MANAGER".equals(role) && !"ADMIN".equals(role)) {
                auditService.log(username, "/api/manager/reports", false, 
                    "Tentativa de acesso negada - não é MANAGER ou ADMIN", request);
                return ResponseEntity.status(403).body(Map.of(
                    "error", "Acesso negado",
                    "message", "Apenas gestores e administradores podem acessar relatórios"
                ));
            }
            
            Map<String, Object> reports = new HashMap<>();
            reports.put("message", "Relatórios de Gestão");
            reports.put("username", username);
            reports.put("role", role);
            reports.put("reports", List.of(
                Map.of("id", 1, "name", "Relatório de Vendas", "status", "disponível"),
                Map.of("id", 2, "name", "Relatório de Performance", "status", "disponível"),
                Map.of("id", 3, "name", "Relatório de Equipe", "status", "disponível")
            ));
            
            auditService.log(username, "/api/manager/reports", true, 
                "Relatórios acessados com sucesso", request);
            
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<?> getManagerDashboard(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(authHeader);
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
            }
            
            String role = jwtUtil.extractRole(authHeader.substring(7));
            if (!"MANAGER".equals(role) && !"ADMIN".equals(role)) {
                auditService.log(username, "/api/manager/dashboard", false, 
                    "Tentativa de acesso negada - não é MANAGER ou ADMIN", request);
                return ResponseEntity.status(403).body(Map.of("error", "Acesso negado"));
            }
            
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("message", "Dashboard de Gestão");
            dashboard.put("username", username);
            dashboard.put("role", role);
            dashboard.put("features", List.of(
                "Visualizar relatórios",
                "Gerenciar equipe",
                "Acessar métricas de performance"
            ));
            
            auditService.log(username, "/api/manager/dashboard", true, 
                "Dashboard de gestão acessado", request);
            
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
}

