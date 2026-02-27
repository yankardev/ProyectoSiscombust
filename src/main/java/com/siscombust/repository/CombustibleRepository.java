package com.siscombust.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.siscombust.entity.Combustible;

@Repository
public interface CombustibleRepository extends JpaRepository<Combustible, Integer> {
	List<Combustible> findByProveedorIdProveedor(Integer idProveedor);
}