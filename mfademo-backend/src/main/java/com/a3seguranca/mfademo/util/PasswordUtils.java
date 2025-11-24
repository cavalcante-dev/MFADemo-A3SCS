package com.a3seguranca.mfademo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtils {
    
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    
    /**
     * Codifica uma senha usando BCrypt
     * @param rawPassword Senha em texto plano
     * @return Senha codificada
     */
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }
    
    /**
     * Verifica se a senha corresponde ao hash
     * @param rawPassword Senha em texto plano
     * @param encodedPassword Senha codificada
     * @return true se corresponder
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
