package com.ctrl.home.controllers;

import com.ctrl.home.models.Proyectos;
import com.ctrl.home.models.Usuario;
import com.ctrl.home.services.IProyectoService;
import com.ctrl.home.services.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    private IProyectoService proyectoService;

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proyectos", proyectoService.listar());
        return "proyectos-home";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proyecto", new Proyectos());
        model.addAttribute("usuarios", usuarioService.listar());
        return "crear-proyecto";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute Proyectos proyecto, @RequestParam("usuarioId") Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId).orElse(null);
        if (usuario != null) {
            proyecto.setUsuario(usuario);
        }
        proyectoService.guardar(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Proyectos proyecto = proyectoService.buscarPorId(id).orElse(null);
        if (proyecto == null) {
            model.addAttribute("error", "Proyecto no encontrado");
            return "acceso-denegado";
        }
        model.addAttribute("proyecto", proyecto);
        return "editar-proyecto";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute Proyectos proyecto) {
        proyecto.setId(id);
        proyectoService.guardar(proyecto);
        return "redirect:/proyectos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
        return "redirect:/proyectos";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") Long id, Model model) {
        Proyectos proyecto = proyectoService.buscarPorId(id).orElse(null);
        if (proyecto == null) {
            model.addAttribute("error", "Proyecto no encontrado");
            return "acceso-denegado";
        }
        model.addAttribute("proyecto", proyecto);
        return "detalle-proyecto";
    }
}