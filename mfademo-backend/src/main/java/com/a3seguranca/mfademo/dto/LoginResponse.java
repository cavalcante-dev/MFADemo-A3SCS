package com.a3seguranca.mfademo.dto;

public record LoginResponse(
    boolean mfaRequired,
    String token,
    String tmpToken,
    String role,
    String message
) {
    public static LoginResponse withToken(String token, String role) {
        return new LoginResponse(false, token, null, role, "Login realizado com sucesso");
    }
    
    public static LoginResponse withMfa(String tmpToken, String role) {
        return new LoginResponse(true, null, tmpToken, role, "MFA requerido. Verifique o código no console.");
    }
}
