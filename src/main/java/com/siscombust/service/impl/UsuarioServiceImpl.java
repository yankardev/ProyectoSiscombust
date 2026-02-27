package com.siscombust.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siscombust.entity.Usuario;
import com.siscombust.repository.UsuarioRepository;
import com.siscombust.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repositorio;

    @Override
    public List<Usuario> listarTodos() {
        return repositorio.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return repositorio.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        return repositorio.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}