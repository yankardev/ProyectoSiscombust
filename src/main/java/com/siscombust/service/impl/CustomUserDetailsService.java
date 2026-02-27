package com.siscombust.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.siscombust.entity.Usuario;
import com.siscombust.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        
        // {noop} le indica a Spring que la contraseña no está encriptada por ahora, ideal para el desarrollo
        return User.builder()
            .username(usuario.getNombreUsuario())
            .password("{noop}" + usuario.getPassword()) 
            // CAMBIO AQUÍ: Usamos authorities en lugar de roles para que coincida exacto con la BD y el HTML
            .authorities(usuario.getRol()) 
            .build();
    }
}