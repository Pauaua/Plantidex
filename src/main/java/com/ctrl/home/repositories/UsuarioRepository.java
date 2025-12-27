package com.ctrl.home.repositories;

import com.ctrl.home.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // MÉTODO NUEVO PARA CONTAR POR ESTADO
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.estado = :estado")
    long countByEstado(@Param("estado") String estado);
    
    }