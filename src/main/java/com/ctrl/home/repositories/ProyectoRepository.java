package com.ctrl.home.repositories;

import com.ctrl.home.models.Proyectos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyectos, Long> {
    // MÉTODO NUEVO PARA CONTAR POR ESTADO
    @Query("SELECT COUNT(p) FROM Proyectos p WHERE p.estado = :estado")
    long countByEstado(@Param("estado") String estado);
    
  
}