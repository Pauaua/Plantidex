package com.ctrl.home.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "proyectos")
public class Proyectos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150, name = "nombre")
    private String nombre;

    @Size(max = 500)
    @Column(length = 500, name = "descripcion") 
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Estado estado = Estado.PLANIFICADO;

    public enum Estado { PLANIFICADO, EN_PROGRESO, COMPLETADO, PAUSADO }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
        @ManyToOne
        @JoinColumn(name = "usuario_id")
        private Usuario usuario;

        public Usuario getUsuario() { return usuario; }
        public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return "Proyectos{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                '}';
    }
}