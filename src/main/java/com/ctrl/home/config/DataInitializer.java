package com.ctrl.home.config;

import com.ctrl.home.models.Usuario;
import com.ctrl.home.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIALIZANDO DATOS DE PRUEBA ===");
        
        // 1. Crear usuario ADMIN si no existe
        if (usuarioRepository.findByEmail("admin@ctrl.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@ctrl.com");
            // Usar PasswordEncoder para encriptar
            admin.setPassword(passwordEncoder.encode("Admin123"));
            admin.setRol("ADMIN");
            admin.setEstado("ACTIVO");
            admin.setDepartamento("Sistemas");
            
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado: admin@ctrl.com / Admin123");
        } else {
            System.out.println("ℹ️  Usuario ADMIN ya existe");
        }

        // 2. Crear usuario USER de prueba si no existe
        if (usuarioRepository.findByEmail("usuario@ctrl.com").isEmpty()) {
            Usuario user = new Usuario();
            user.setNombre("Usuario de Prueba");
            user.setEmail("usuario@ctrl.com");
            user.setPassword(passwordEncoder.encode("User123"));
            user.setRol("USER");
            user.setEstado("ACTIVO");
            user.setDepartamento("Operaciones");
            
            usuarioRepository.save(user);
            System.out.println("✅ Usuario USER creado: usuario@ctrl.com / User123");
        } else {
            System.out.println("ℹ️  Usuario USER ya existe");
        }
        
        // 3. Mostrar conteo de usuarios
        long totalUsuarios = usuarioRepository.count();
        System.out.println("📊 Total de usuarios en BD: " + totalUsuarios);
        System.out.println("=== FIN INICIALIZACIÓN ===");
    }
}