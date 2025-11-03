
package com.ctrl.home.controllers;

import com.ctrl.home.models.Usuario;
import com.ctrl.home.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AppController {
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioLogueado", true);
            session.setAttribute("email", usuario.getEmail());
            session.setAttribute("nombre", usuario.getNombre());
            session.setAttribute("rol", usuario.getRol());
            session.setAttribute("userId", usuario.getId());
            return "redirect:/dashboard";
        }
        return "redirect:/login?error=true";
    }
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- MÉTODOS DE AUTENTICACIÓN SIMULADA ---

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String rol,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String departamento) {
        try {
            // Validar confirmación de contraseña
            if (!password.equals(confirmPassword)) {
                return "redirect:/registro?error=pass";
            }
            // Validar rol
            if (!"ADMIN".equals(rol) && !"USER".equals(rol)) {
                return "redirect:/registro?error=rol";
            }
            // Verificar si el email ya existe
            if (usuarioRepository.existsByEmail(email)) {
                return "redirect:/registro?error=email";
            }
            // Crear nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(password);
            usuario.setTelefono(telefono);
            usuario.setDepartamento(departamento);
            usuario.setRol(rol);
            usuario.setEstado("ACTIVO");
            usuarioRepository.save(usuario);
            return "redirect:/login?registroExitoso=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/registro?error=general";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --- MÉTODOS PARA MOSTRAR LAS VISTAS ---

    @GetMapping("/")
    public String mostrarIndex() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    @GetMapping("/acceso-denegado")
    public String mostrarAccesoDenegado() {
        return "acceso-denegado";
    }
}