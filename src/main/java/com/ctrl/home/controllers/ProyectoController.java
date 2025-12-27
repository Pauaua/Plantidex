package com.ctrl.home.controllers;

import com.ctrl.home.models.Proyectos;
import com.ctrl.home.models.Usuario;
import com.ctrl.home.services.IProyectoService;
import com.ctrl.home.services.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    private IProyectoService proyectoService;

    @Autowired
    private IUsuarioService usuarioService;

    // LISTAR PROYECTOS - CUALQUIER USUARIO AUTENTICADO
    @GetMapping
    public String listar(Model model) {
        List<Proyectos> proyectos = proyectoService.listar();
        model.addAttribute("proyectos", proyectos);
        return "proyectos-home";
    }

    // FORMULARIO NUEVO PROYECTO - SOLO ADMIN
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String nuevo(Model model) {
        model.addAttribute("proyecto", new Proyectos());
        model.addAttribute("usuarios", usuarioService.listar());
        return "crear-proyecto";
    }

    // GUARDAR PROYECTO - SOLO ADMIN con VALIDACIONES
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(@Valid @ModelAttribute Proyectos proyecto, 
                         BindingResult bindingResult,
                         @RequestParam("usuarioId") Long usuarioId,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarios", usuarioService.listar());
            return "crear-proyecto";
        }

        Usuario usuario = usuarioService.buscarPorId(usuarioId).orElse(null);
        if (usuario != null) {
            proyecto.setUsuario(usuario);
        }

        proyectoService.guardar(proyecto);
        redirectAttributes.addFlashAttribute("success", "Proyecto creado exitosamente");
        return "redirect:/proyectos";
    }

    // FORMULARIO EDITAR - SOLO ADMIN
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model) {
        Proyectos proyecto = proyectoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        model.addAttribute("proyecto", proyecto);
        return "editar-proyecto";
    }

    // ACTUALIZAR PROYECTO - SOLO ADMIN con VALIDACIONES
    @PostMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizar(@PathVariable Long id,
                           @Valid @ModelAttribute Proyectos proyecto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "editar-proyecto";
        }

        proyecto.setId(id);
        proyectoService.guardar(proyecto);
        redirectAttributes.addFlashAttribute("success", "Proyecto actualizado exitosamente");
        return "redirect:/proyectos";
    }

    // ELIMINAR PROYECTO - SOLO ADMIN
    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        proyectoService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Proyecto eliminado exitosamente");
        return "redirect:/proyectos";
    }

    // DETALLE PROYECTO - CUALQUIER USUARIO AUTENTICADO
    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") Long id, Model model) {
        Proyectos proyecto = proyectoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        model.addAttribute("proyecto", proyecto);
        return "detalle-proyecto";
    }
}