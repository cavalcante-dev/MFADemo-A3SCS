package com.a3seguranca.mfademo.config;

import com.a3seguranca.mfademo.entity.UserEntity;
import com.a3seguranca.mfademo.repository.UserRepository;
import com.a3seguranca.mfademo.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Limpar usuários existentes (apenas para demo)
        if (userRepository.count() == 0) {
            createUsers();
        }
    }
    
    private void createUsers() {
        // ADMIN com MFA habilitado
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(PasswordUtils.encode("admin123"));
        admin.setRole("ADMIN");
        admin.setEnabled(true);
        admin.setMfaEnabled(true);
        admin.setEmail("admin@example.com");
        userRepository.save(admin);
        System.out.println("✓ Usuário criado: admin / admin123 (MFA: ON)");
        
        // MANAGER com MFA habilitado
        UserEntity manager = new UserEntity();
        manager.setUsername("manager");
        manager.setPassword(PasswordUtils.encode("manager123"));
        manager.setRole("MANAGER");
        manager.setEnabled(true);
        manager.setMfaEnabled(true);
        manager.setEmail("manager@example.com");
        userRepository.save(manager);
        System.out.println("✓ Usuário criado: manager / manager123 (MFA: ON)");
        
        // USER comum com MFA habilitado
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword(PasswordUtils.encode("user123"));
        user.setRole("USER");
        user.setEnabled(true);
        user.setMfaEnabled(true);
        user.setEmail("user@example.com");
        userRepository.save(user);
        System.out.println("✓ Usuário criado: user / user123 (MFA: ON)");
        
        // CREDENCIAL VAZADA - usuário sem MFA para demonstração de ataque
        UserEntity leaked = new UserEntity();
        leaked.setUsername("vazado");
        leaked.setPassword(PasswordUtils.encode("senha123"));
        leaked.setRole("USER");
        leaked.setEnabled(true);
        leaked.setMfaEnabled(false); // SEM MFA - vulnerável!
        leaked.setEmail("vazado@example.com");
        userRepository.save(leaked);
        System.out.println("⚠ CREDENCIAL VAZADA criada: vazado / senha123 (MFA: OFF)");
        System.out.println("   Este usuário será usado para demonstrar o ataque!");
        
        // Usuário comum sem MFA (para comparação)
        UserEntity userNoMfa = new UserEntity();
        userNoMfa.setUsername("usernomfa");
        userNoMfa.setPassword(PasswordUtils.encode("senha123"));
        userNoMfa.setRole("USER");
        userNoMfa.setEnabled(true);
        userNoMfa.setMfaEnabled(false);
        userNoMfa.setEmail("usernomfa@example.com");
        userRepository.save(userNoMfa);
        System.out.println("✓ Usuário criado: usernomfa / senha123 (MFA: OFF)");
        
        System.out.println("\n========================================");
        System.out.println("Usuários de demonstração criados:");
        System.out.println("========================================");
        System.out.println("ADMIN:    admin / admin123 (MFA: ON)");
        System.out.println("MANAGER:  manager / manager123 (MFA: ON)");
        System.out.println("USER:     user / user123 (MFA: ON)");
        System.out.println("VAZADO:   vazado / senha123 (MFA: OFF) ⚠");
        System.out.println("NO_MFA:   usernomfa / senha123 (MFA: OFF)");
        System.out.println("========================================\n");
    }
}

