package com.siscombust.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.siscombust.entity.Consumo;

@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, Integer> {
	
	// Busca consumos entre dos fechas
    java.util.List<Consumo> findByFechaConsumoBetween(java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin);
}