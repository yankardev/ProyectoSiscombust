package com.siscombust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.siscombust.entity.Vehiculo;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
	
    java.util.List<Vehiculo> findByMarcaContainingIgnoreCase(String marca);
}