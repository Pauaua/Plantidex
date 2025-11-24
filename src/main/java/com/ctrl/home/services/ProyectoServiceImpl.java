package com.ctrl.home.services;

import com.ctrl.home.models.Proyectos;
import com.ctrl.home.repositories.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoServiceImpl implements IProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Override
    public Proyectos guardar(Proyectos proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public List<Proyectos> listar() {
        return proyectoRepository.findAll();
    }

    @Override
    public Optional<Proyectos> buscarPorId(Long id) {
        return proyectoRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        proyectoRepository.deleteById(id);
    }
}