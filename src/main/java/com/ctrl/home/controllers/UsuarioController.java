// src/main/java/com/ctrl/home/controllers/UsuarioController.java
package com.ctrl.home.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrl.home.models.Usuario;
import com.ctrl.home.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    // Dashboard principal - Lista de usuarios (solo administradores)
    @GetMapping
    public String listarUsuarios(Model model, HttpSession session) {
        // Verificar si el usuario está logueado
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login?error=no-autenticado";
        }
        // Verificar si el usuario es administrador
        String rol = (String) session.getAttribute("rol");
        if (!"ADMIN".equals(rol)) {
            // Redirigir al dashboard si no es admin
            return "redirect:/dashboard?error=permiso";
        }
        // Agregar información del usuario a la vista
        model.addAttribute("usuarioEmail", session.getAttribute("email"));
        model.addAttribute("usuarioNombre", session.getAttribute("nombre"));
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios";
    }

    // Formulario para crear usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null || 
            !"ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", new Usuario());
        return "crear-usuario";
    }

    // Procesar creación de usuario
    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute Usuario usuario, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null || 
            !"ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/login";
        }
        
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return "redirect:/usuarios/nuevo?error=email-existe";
        }

        usuarioRepository.save(usuario);
        return "redirect:/usuarios?success=usuario-creado";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null || 
            !"ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "editar-usuario";
    }

    // Actualizar usuario
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, 
                                  @ModelAttribute Usuario usuarioActualizado,
                                  HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null || 
            !"ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el email ha cambiado y ya existe
        if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) &&
            usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            return "redirect:/usuarios/editar/" + id + "?error=email-existe";
        }

        // Validar campos obligatorios
        if (usuarioActualizado.getNombre() == null || usuarioActualizado.getNombre().trim().isEmpty() ||
            usuarioActualizado.getEmail() == null || usuarioActualizado.getEmail().trim().isEmpty() ||
            usuarioActualizado.getRol() == null || usuarioActualizado.getRol().trim().isEmpty() ||
            usuarioActualizado.getEstado() == null || usuarioActualizado.getEstado().trim().isEmpty()) {
            return "redirect:/usuarios/editar/" + id + "?error=campos-obligatorios";
        }

        // Actualizar campos
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setDepartamento(usuarioActualizado.getDepartamento());
        usuario.setEstado(usuarioActualizado.getEstado());

        // Actualizar contraseña solo si se proporciona una nueva
        String newPassword = usuarioActualizado.getPassword();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            usuario.setPassword(newPassword);
        } else {
            // Si no se proporciona, mantener la actual
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                return "redirect:/usuarios/editar/" + id + "?error=pass-obligatoria";
            }
        }

        usuarioRepository.save(usuario);
        return "redirect:/usuarios?success=usuario-actualizado";
    }

    // Eliminar usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null || 
            !"ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/login";
        }

        usuarioRepository.deleteById(id);
        return "redirect:/usuarios?success=usuario-eliminado";
    }

    // Vista detallada de usuario
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null || 
            !"ADMIN".equals(session.getAttribute("rol"))) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "detalle-usuario";
    }
}