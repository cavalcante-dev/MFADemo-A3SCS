package com.a3seguranca.mfademo.service;

import com.a3seguranca.mfademo.entity.AccessLog;
import com.a3seguranca.mfademo.repository.AccessLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {
    
    @Autowired
    private AccessLogRepository accessLogRepository;
    
    /**
     * Registra um log de auditoria
     * @param username Nome do usuário
     * @param endpoint Endpoint acessado
     * @param success Se o acesso foi bem-sucedido
     * @param message Mensagem adicional
     */
    public void log(String username, String endpoint, boolean success, String message) {
        log(username, endpoint, success, message, null);
    }
    
    /**
     * Registra um log de auditoria com IP
     * @param username Nome do usuário
     * @param endpoint Endpoint acessado
     * @param success Se o acesso foi bem-sucedido
     * @param message Mensagem adicional
     * @param request Request HTTP para extrair IP
     */
    public void log(String username, String endpoint, boolean success, String message, HttpServletRequest request) {
        AccessLog log = new AccessLog();
        log.setUsername(username);
        log.setEndpoint(endpoint);
        log.setSuccess(success);
        log.setMessage(message);
        log.setTimestamp(LocalDateTime.now());
        
        if (request != null) {
            String ipAddress = getClientIpAddress(request);
            log.setIpAddress(ipAddress);
        }
        
        accessLogRepository.save(log);
        
        // Log no console também para demonstração
        String status = success ? "✓ SUCESSO" : "✗ NEGADO";
        System.out.println(String.format("[AUDIT] %s - %s - %s - %s - %s", 
            log.getTimestamp(), status, username, endpoint, message));
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
