package com.siscombust.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siscombust.entity.Consumo;
import com.siscombust.service.ConsumoService;

@Controller
public class AuthController {

    @Autowired
    private ConsumoService consumoService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/api/dashboard")
    @ResponseBody
    public Map<String, Object> obtenerDatosDashboard() {
        List<Consumo> consumos = consumoService.listarTodos();
        
        Map<String, Double> galonesPorVehiculo = new HashMap<>();
        Map<String, Double> gastoPorProveedor = new HashMap<>();
        
        for (Consumo c : consumos) {
            String placa = (c.getVehiculo() != null) ? c.getVehiculo().getPlaca() : "Sin Placa";
            double galones = c.getCantidadGalones() != null ? c.getCantidadGalones().doubleValue() : 0.0;
            galonesPorVehiculo.put(placa, galonesPorVehiculo.getOrDefault(placa, 0.0) + galones);
            
            String proveedor = (c.getProveedor() != null) ? c.getProveedor().getNombreProveedor() : "Sin Proveedor";
            double dinero = c.getTotalSoles() != null ? c.getTotalSoles().doubleValue() : 0.0;
            gastoPorProveedor.put(proveedor, gastoPorProveedor.getOrDefault(proveedor, 0.0) + dinero);
        }
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("labelsVehiculos", galonesPorVehiculo.keySet());
        respuesta.put("dataVehiculos", galonesPorVehiculo.values());
        respuesta.put("labelsProveedores", gastoPorProveedor.keySet());
        respuesta.put("dataProveedores", gastoPorProveedor.values());
        
        return respuesta;
    }
}