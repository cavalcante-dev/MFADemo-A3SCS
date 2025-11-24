package com.a3seguranca.mfademo.dto;

import jakarta.validation.constraints.NotBlank;

public record MfaVerifyRequest(
    @NotBlank(message = "Token temporário é obrigatório")
    String tmpToken,
    
    @NotBlank(message = "Código MFA é obrigatório")
    String code
) {}
