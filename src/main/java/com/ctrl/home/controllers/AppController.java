package com.ctrl.home.controllers;

import com.ctrl.home.repositories.UsuarioRepository;
import com.ctrl.home.repositories.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProyectoRepository proyectoRepository;

    @GetMapping("/")
    public String mostrarIndex() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        // Estadísticas de usuarios - AHORA FUNCIONARÁ
        long totalUsuarios = usuarioRepository.count();
        long usuariosActivos = usuarioRepository.countByEstado("ACTIVO");
        
        // Estadísticas de proyectos - AHORA FUNCIONARÁ
        long totalProyectos = proyectoRepository.count();
        long proyectosActivos = proyectoRepository.countByEstado("ACTIVO");
        
        // Pasar datos al modelo
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("totalProyectos", totalProyectos);
        model.addAttribute("proyectosActivos", proyectosActivos);
        
        return "dashboard";
    }

    @GetMapping("/acceso-denegado")
    public String mostrarAccesoDenegado() {
        return "acceso-denegado";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }
}