package com.siscombust.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.siscombust.entity.Vehiculo;
import com.siscombust.service.VehiculoService;
import com.siscombust.service.impl.ReporteService;
import com.siscombust.service.CombustibleService;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;
    
    @Autowired
    private CombustibleService combustibleService;
    
    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("vehiculos", vehiculoService.listarTodos());
        return "vehiculo/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        modelo.addAttribute("vehiculo", new com.siscombust.entity.Vehiculo());
        List<com.siscombust.entity.Combustible> todosLosCombustibles = combustibleService.listarTodos();
        Map<String, com.siscombust.entity.Combustible> combustiblesUnicos = new HashMap<>();
        
        for (com.siscombust.entity.Combustible c : todosLosCombustibles) {

            if (!combustiblesUnicos.containsKey(c.getNombre())) {
                combustiblesUnicos.put(c.getNombre(), c);
            }
        }
        
        modelo.addAttribute("combustibles", new ArrayList<>(combustiblesUnicos.values()));
        return "vehiculo/nuevo";
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute("vehiculo") Vehiculo vehiculo, RedirectAttributes redirectAttrs) {

        if (vehiculo.getPlaca() != null) {
            vehiculo.setPlaca(vehiculo.getPlaca().toUpperCase());
        }
        if (vehiculo.getMarca() != null) {
            vehiculo.setMarca(vehiculo.getMarca().toUpperCase());
        }
        if (vehiculo.getModelo() != null) {
            vehiculo.setModelo(vehiculo.getModelo().toUpperCase());
        }
        
        vehiculoService.guardar(vehiculo);
        redirectAttrs.addFlashAttribute("mensajeExito", "Vehículo guardado correctamente.");
        return "redirect:/vehiculos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("vehiculo", vehiculoService.buscarPorId(id));

        List<com.siscombust.entity.Combustible> todosLosCombustibles = combustibleService.listarTodos();
        Map<String, com.siscombust.entity.Combustible> combustiblesUnicos = new HashMap<>();
        
        for (com.siscombust.entity.Combustible c : todosLosCombustibles) {
            if (!combustiblesUnicos.containsKey(c.getNombre())) {
                combustiblesUnicos.put(c.getNombre(), c);
            }
        }
        
        modelo.addAttribute("combustibles", new ArrayList<>(combustiblesUnicos.values()));

        return "vehiculo/editar";
    }

 
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
        	vehiculoService.eliminar(id);
            redirectAttrs.addFlashAttribute("mensajeExito", "Vehículo eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {

            redirectAttrs.addFlashAttribute("mensajeError", "Error: No se puede eliminar este vehículo porque ya tiene abastecimientos registrados en el Historial de Consumos.");
        } catch (Exception e) {

            redirectAttrs.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al intentar eliminar.");
        }
        return "redirect:/vehiculos";
    }
    

    @GetMapping("/reporte")
    public org.springframework.http.ResponseEntity<byte[]> descargarInventario() throws Exception {
        java.util.List<com.siscombust.entity.Vehiculo> listaVehiculos = vehiculoService.listarTodos();
        
        byte[] pdf = reporteService.generarReporteVehiculos(listaVehiculos);
        
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Inventario_Flota.pdf");
        
        return new org.springframework.http.ResponseEntity<>(pdf, headers, org.springframework.http.HttpStatus.OK);
    }
}