package com.ctrl.home.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria", groups = {CreateValidation.class})
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres", groups = {CreateValidation.class})
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "El rol debe ser ADMIN o USER")
    private String rol;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(ACTIVO|INACTIVO)$", message = "El estado debe ser ACTIVO o INACTIVO")
    private String estado = "ACTIVO";

    private String telefono;
    private String departamento;

    // Interface para validación grupal
    public interface CreateValidation {}
    public interface UpdateValidation {}

    // Constructor vacío
    public Usuario() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { 
        // Encriptar automáticamente si no está vacía
        if (password != null && !password.trim().isEmpty() && !password.startsWith("$2a$")) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            this.password = encoder.encode(password);
        } else {
            this.password = password;
        }
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    // Método para encriptar password sin modificar el campo directamente
    public void encryptPassword() {
        if (this.password != null && !this.password.trim().isEmpty() && !this.password.startsWith("$2a$")) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            this.password = encoder.encode(this.password);
        }
    }
}