package com.ctrl.home.repositories;

import com.ctrl.home.models.Proyectos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyectos, Long> {
}