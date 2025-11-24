// src/main/java/com/ctrl/home/controllers/UsuarioController.java
package com.ctrl.home.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrl.home.models.Usuario;
import com.ctrl.home.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    // Utilidad para verificar rol ADMIN
    private boolean esAdmin(HttpSession session) {
        Object logueado = session.getAttribute("usuarioLogueado");
        Object rolObj = session.getAttribute("rol");
        String rol = rolObj != null ? rolObj.toString().trim().toUpperCase() : "";
        return logueado != null && Boolean.TRUE.equals(logueado) && "ADMIN".equals(rol);
    }

    // Lista de usuarios (solo administradores)
    @GetMapping
    public String listarUsuarios(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/login?error=no-autenticado";
        }
        model.addAttribute("usuarioEmail", session.getAttribute("email"));
        model.addAttribute("usuarioNombre", session.getAttribute("nombre"));
        model.addAttribute("usuarios", usuarioService.listar());
        return "usuarios";
    }

    // Formulario para crear usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", new Usuario());
        return "crear-usuario";
    }

    // Procesar creación de usuario
    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute Usuario usuario, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/login";
        }
        if (usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()) {
            return "redirect:/usuarios/nuevo?error=email-existe";
        }
        usuarioService.guardar(usuario);
        return "redirect:/usuarios?success=usuario-creado";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/login";
        }
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "editar-usuario";
    }

    // Actualizar usuario
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
                                  @ModelAttribute Usuario usuarioActualizado,
                                  HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/login";
        }
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) &&
            usuarioService.buscarPorEmail(usuarioActualizado.getEmail()).isPresent()) {
            return "redirect:/usuarios/editar/" + id + "?error=email-existe";
        }

        if (usuarioActualizado.getNombre() == null || usuarioActualizado.getNombre().trim().isEmpty() ||
            usuarioActualizado.getEmail() == null || usuarioActualizado.getEmail().trim().isEmpty() ||
            usuarioActualizado.getRol() == null || usuarioActualizado.getRol().trim().isEmpty() ||
            usuarioActualizado.getEstado() == null || usuarioActualizado.getEstado().trim().isEmpty()) {
            return "redirect:/usuarios/editar/" + id + "?error=campos-obligatorios";
        }

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setTelefono(usuarioActualizado.getTelefono() != null && !usuarioActualizado.getTelefono().trim().isEmpty() ? usuarioActualizado.getTelefono() : null);
        usuario.setDepartamento(usuarioActualizado.getDepartamento());
        usuario.setEstado(usuarioActualizado.getEstado());

        String newPassword = usuarioActualizado.getPassword();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            usuario.setPassword(newPassword);
        } else {
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                return "redirect:/usuarios/editar/" + id + "?error=pass-obligatoria";
            }
        }

        usuarioService.guardar(usuario);
        return "redirect:/usuarios?success=usuario-actualizado";
    }

    // Eliminar usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/login";
        }
        usuarioService.eliminar(id);
        return "redirect:/usuarios?success=usuario-eliminado";
    }

    // Vista detallada de usuario
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/login";
        }
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "detalle-usuario";
    }

    // login 
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        boolean ok = usuarioService.validarLogin(email, password);
        if (!ok) {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
        Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);
        session.setAttribute("usuarioLogueado", true);
        session.setAttribute("email", usuario.getEmail());
        session.setAttribute("nombre", usuario.getNombre());
        session.setAttribute("rol", usuario.getRol().trim().toUpperCase()); // Normaliza el rol
        return "redirect:/dashboard";
    }
}