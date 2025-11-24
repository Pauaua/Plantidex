package com.ctrl.home.services;

import com.ctrl.home.models.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    Usuario guardar(Usuario usuario);
    List<Usuario> listar();
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    void eliminar(Long id);
    boolean validarLogin(String email, String password);
}