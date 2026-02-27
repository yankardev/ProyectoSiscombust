package com.siscombust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.siscombust.entity.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    // JpaRepository ya incluye los métodos findAll(), save(), findById(), deleteById()
}