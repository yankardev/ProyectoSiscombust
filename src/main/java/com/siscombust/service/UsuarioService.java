package com.siscombust.service;

import java.util.List;
import com.siscombust.entity.Usuario;

public interface UsuarioService {
    List<Usuario> listarTodos();
    Usuario guardar(Usuario usuario);
    Usuario buscarPorId(Integer id);
    void eliminar(Integer id);
}