package com.a3seguranca.mfademo.dto;

public record UserDTO(
    Long id,
    String username,
    String role,
    boolean enabled,
    boolean mfaEnabled,
    String email
) {}
