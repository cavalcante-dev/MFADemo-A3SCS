package com.a3seguranca.mfademo.service;

import com.a3seguranca.mfademo.entity.UserEntity;
import com.a3seguranca.mfademo.repository.UserRepository;
import com.a3seguranca.mfademo.security.JWTUtil;
import com.a3seguranca.mfademo.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MfaService mfaService;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    /**
     * Realiza login do usuário
     * @param username Nome do usuário
     * @param password Senha
     * @param request Request HTTP para auditoria
     * @return LoginResponse com token ou indicação de MFA necessário
     */
    public LoginResult login(String username, String password, HttpServletRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            auditService.log(username, "/auth/login", false, 
                "Tentativa de login com usuário inexistente", request);
            throw new RuntimeException("Credenciais inválidas");
        }
        
        UserEntity user = userOpt.get();
        
        if (!user.isEnabled()) {
            auditService.log(username, "/auth/login", false, 
                "Tentativa de login com conta desabilitada", request);
            throw new RuntimeException("Conta desabilitada");
        }
        
        if (!PasswordUtils.matches(password, user.getPassword())) {
            auditService.log(username, "/auth/login", false, 
                "Tentativa de login com senha incorreta", request);
            throw new RuntimeException("Credenciais inválidas");
        }
        
        // Se MFA está habilitado, iniciar processo MFA
        if (user.isMfaEnabled()) {
            String tmpToken = mfaService.startMfa(username);
            auditService.log(username, "/auth/login", false, 
                "Login iniciado - MFA requerido", request);
            return new LoginResult(true, null, tmpToken, user.getRole());
        }
        
        // Login sem MFA - gerar token diretamente
        String token = jwtUtil.generateToken(username, user.getRole());
        auditService.log(username, "/auth/login", true, 
            "Login bem-sucedido (sem MFA)", request);
        return new LoginResult(false, token, null, user.getRole());
    }
    
    /**
     * Verifica código MFA e retorna token final
     * @param tmpToken Token temporário
     * @param code Código MFA
     * @param request Request HTTP para auditoria
     * @return Token JWT final
     */
    public String verifyMfa(String tmpToken, String code, HttpServletRequest request) {
        String username = mfaService.verify(tmpToken, code);
        
        if (username == null) {
            auditService.log("unknown", "/auth/mfa/verify", false, 
                "Tentativa de verificação MFA com código inválido", request);
            throw new RuntimeException("Código MFA inválido");
        }
        
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            auditService.log(username, "/auth/mfa/verify", false, 
                "Usuário não encontrado após verificação MFA", request);
            throw new RuntimeException("Erro interno");
        }
        
        UserEntity user = userOpt.get();
        String token = jwtUtil.generateToken(username, user.getRole());
        auditService.log(username, "/auth/mfa/verify", true, 
            "MFA verificado com sucesso - login completo", request);
        
        return token;
    }
    
    /**
     * Classe interna para resultado do login
     */
    public static class LoginResult {
        private final boolean mfaRequired;
        private final String token;
        private final String tmpToken;
        private final String role;
        
        public LoginResult(boolean mfaRequired, String token, String tmpToken, String role) {
            this.mfaRequired = mfaRequired;
            this.token = token;
            this.tmpToken = tmpToken;
            this.role = role;
        }
        
        public boolean isMfaRequired() {
            return mfaRequired;
        }
        
        public String getToken() {
            return token;
        }
        
        public String getTmpToken() {
            return tmpToken;
        }
        
        public String getRole() {
            return role;
        }
    }
}
