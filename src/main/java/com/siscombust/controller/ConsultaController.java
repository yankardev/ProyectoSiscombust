package com.siscombust.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.siscombust.service.ConsumoService;
import com.siscombust.service.VehiculoService;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private VehiculoService vehiculoService;
    
    @Autowired
    private ConsumoService consumoService;

    // CONSULTA 1: Vehículos por Marca
    @GetMapping("/vehiculos")
    public String consultaVehiculos(@RequestParam(required = false) String marca, Model modelo) {
        if (marca != null && !marca.trim().isEmpty()) {
            modelo.addAttribute("vehiculos", vehiculoService.buscarPorMarca(marca));
            modelo.addAttribute("marcaBuscada", marca);
        } else {
            modelo.addAttribute("vehiculos", new ArrayList<>()); // Lista vacía al inicio
        }
        return "consulta/vehiculos";
    }

    // CONSULTA 2: Consumos por Fechas
    @GetMapping("/consumos")
    public String consultaConsumos(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            Model modelo) {
        
        if (fechaInicio != null && fechaFin != null) {
            modelo.addAttribute("consumos", consumoService.buscarPorFechas(fechaInicio, fechaFin));
            modelo.addAttribute("fechaInicio", fechaInicio);
            modelo.addAttribute("fechaFin", fechaFin);
        } else {
            modelo.addAttribute("consumos", new ArrayList<>());
        }
        return "consulta/consumos";
    }
}