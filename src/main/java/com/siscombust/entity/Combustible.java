package com.siscombust.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "combustibles")
public class Combustible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_combustible")
    private Integer idCombustible;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @Column(name = "precio_actual", nullable = false, precision = 8, scale = 2)
    private BigDecimal precioActual;
    
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

}