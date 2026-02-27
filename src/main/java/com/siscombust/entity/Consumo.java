package com.siscombust.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "consumos")
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consumo")
    private Integer idConsumo;

    @Column(name = "fecha_consumo", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Obligatorio para leer fechas de HTML
    private LocalDate fechaConsumo;

    // Relaciones con las otras tablas
    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_combustible", nullable = false)
    private Combustible combustible;

    @Column(name = "cantidad_galones", nullable = false, precision = 8, scale = 2)
    private BigDecimal cantidadGalones;

    @Column(name = "total_soles", nullable = false, precision = 8, scale = 2)
    private BigDecimal totalSoles;
    
    @Column(name = "usuario_registro", length = 50)
    private String usuarioRegistro;
}