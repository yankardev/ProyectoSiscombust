package com.siscombust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.siscombust.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Spring Data JPA creará automáticamente la consulta SQL (SELECT * FROM usuarios WHERE nombre_usuario = ?)
    Usuario findByNombreUsuario(String nombreUsuario);
    
}