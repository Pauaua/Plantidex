package com.ctrl.home.services;

import com.ctrl.home.models.Proyectos;
import java.util.List;
import java.util.Optional;

public interface IProyectoService {
    Proyectos guardar(Proyectos proyecto);
    List<Proyectos> listar();
    Optional<Proyectos> buscarPorId(Long id);
    void eliminar(Long id);
}