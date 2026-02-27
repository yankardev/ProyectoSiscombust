package com.siscombust.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Integer idVehiculo;

    @Column(name = "placa", nullable = false, unique = true, length = 15)
    private String placa;

    @Column(name = "marca", nullable = false, length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "capacidad_galones", nullable = false, precision = 8, scale = 2)
    private BigDecimal capacidadGalones;
    
    @ManyToOne
    @JoinColumn(name = "id_combustible")
    private Combustible combustible;
    
    @Column(name = "estado", length = 30)
    private String estado = "Activo"; // 
}