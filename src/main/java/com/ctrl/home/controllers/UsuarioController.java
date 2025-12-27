package com.ctrl.home.controllers;

import com.ctrl.home.models.Usuario;
import com.ctrl.home.services.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    // LISTAR USUARIOS - SOLO ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listar();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    // FORMULARIO CREAR USUARIO - SOLO ADMIN
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "crear-usuario";
    }

    // PROCESAR CREACIÓN - SOLO ADMIN con VALIDACIONES
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public String crearUsuario(
            @Validated(Usuario.CreateValidation.class) @ModelAttribute Usuario usuario,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "crear-usuario";
        }

        // Verificar si el email ya existe
        if (usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("errorEmail", "El email ya está registrado");
            return "crear-usuario";
        }

        // Encriptar contraseña antes de guardar
        usuario.encryptPassword();
        
        // Asegurar formato correcto del rol
        if (usuario.getRol() != null) {
            usuario.setRol(usuario.getRol().toUpperCase());
        }
        
        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        return "redirect:/usuarios";
    }

    // FORMULARIO EDITAR - SOLO ADMIN
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "editar-usuario";
    }

    // PROCESAR ACTUALIZACIÓN - SOLO ADMIN con VALIDACIONES
    @PostMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizarUsuario(
            @PathVariable Long id,
            @Valid @ModelAttribute Usuario usuarioActualizado,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "editar-usuario";
        }

        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar cambio de email
        if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) &&
            usuarioService.buscarPorEmail(usuarioActualizado.getEmail()).isPresent()) {
            model.addAttribute("errorEmail", "El email ya está registrado");
            return "editar-usuario";
        }

        // Actualizar campos
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setRol(usuarioActualizado.getRol().toUpperCase());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setDepartamento(usuarioActualizado.getDepartamento());
        usuario.setEstado(usuarioActualizado.getEstado());

        // Solo actualizar password si se proporcionó una nueva
        if (usuarioActualizado.getPassword() != null && 
            !usuarioActualizado.getPassword().trim().isEmpty()) {
            usuario.setPassword(usuarioActualizado.getPassword());
        }

        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
        return "redirect:/usuarios";
    }

    // ELIMINAR USUARIO - SOLO ADMIN
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        return "redirect:/usuarios";
    }

    // DETALLE USUARIO - SOLO ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String verDetalle(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "detalle-usuario";
    }
}