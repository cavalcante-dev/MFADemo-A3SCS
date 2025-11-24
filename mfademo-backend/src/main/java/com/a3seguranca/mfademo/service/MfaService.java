package com.a3seguranca.mfademo.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MfaService {
    
    private final Map<String, String> tmpTokenToUsername = new ConcurrentHashMap<>();
    private final Map<String, String> codeStore = new ConcurrentHashMap<>();
    private final Random random = new Random();
    
    /**
     * Inicia o processo MFA para um usuário
     * @param username Nome do usuário
     * @return Token temporário para verificação
     */
    public String startMfa(String username) {
        String tmpToken = UUID.randomUUID().toString();
        String code = String.format("%06d", random.nextInt(1_000_000));
        
        tmpTokenToUsername.put(tmpToken, username);
        codeStore.put(username, code);
        
        // Simula envio: logar no console
        System.out.println("========================================");
        System.out.println("=== MFA CODE for " + username + " : " + code + " ===");
        System.out.println("========================================");
        
        return tmpToken;
    }
    
    /**
     * Verifica o código MFA
     * @param tmpToken Token temporário
     * @param code Código fornecido pelo usuário
     * @return Username se válido, null caso contrário
     */
    public String verify(String tmpToken, String code) {
        String username = tmpTokenToUsername.get(tmpToken);
        if (username == null) {
            return null;
        }
        
        String expected = codeStore.get(username);
        boolean ok = expected != null && expected.equals(code);
        
        if (ok) {
            // Limpar dados temporários
            tmpTokenToUsername.remove(tmpToken);
            codeStore.remove(username);
            return username;
        }
        
        return null;
    }
    
    /**
     * Remove tokens expirados (opcional, para limpeza)
     */
    public void cleanup() {
        // Em produção, implementar TTL ou scheduler
        // Por enquanto, mantém em memória durante a sessão
    }
}
