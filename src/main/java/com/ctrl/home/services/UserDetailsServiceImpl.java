package com.ctrl.home.services;

import com.ctrl.home.models.Usuario;
import com.ctrl.home.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Verificar que el usuario esté activo
        if (!"ACTIVO".equals(usuario.getEstado())) {
            throw new UsernameNotFoundException("Usuario inactivo: " + email);
        }

        // Crear authorities basadas en el rol
        List<GrantedAuthority> authorities = new ArrayList<>();
        String role = usuario.getRol().toUpperCase();
        
        // Asegurar que el rol tenga el prefijo "ROLE_" para Spring Security
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        authorities.add(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(
            usuario.getEmail(),
            usuario.getPassword(), // ¡DEBE estar encriptada con BCrypt!
            true, true, true, true, // enabled, accountNonExpired, credentialsNonExpired, accountNonLocked
            authorities
        );
    }
}