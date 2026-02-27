package com.siscombust.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.siscombust.entity.Combustible;
import com.siscombust.service.CombustibleService;
import com.siscombust.service.ProveedorService; 

@Controller
@RequestMapping("/combustibles")
public class CombustibleController {

    @Autowired
    private CombustibleService combustibleService; 

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("combustibles", combustibleService.listarTodos());
        return "combustible/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        modelo.addAttribute("combustible", new Combustible());
        
        modelo.addAttribute("proveedores", proveedorService.listarTodos()); 
        return "combustible/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("combustible") Combustible combustible) {
        combustibleService.guardar(combustible);
        return "redirect:/combustibles";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model modelo) {
        
        modelo.addAttribute("combustible", combustibleService.buscarPorId(id));
        modelo.addAttribute("proveedores", proveedorService.listarTodos()); 
        return "combustible/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            combustibleService.eliminar(id);
            redirectAttrs.addFlashAttribute("mensajeExito", "Combustible eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttrs.addFlashAttribute("mensajeError", "ERROR: No se puede eliminar este combustible porque actualmente está asignado a uno o más vehículos en tu flota.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al intentar eliminar el registro.");
        }
        return "redirect:/combustibles";
    }
    
    @GetMapping("/por-proveedor/{id}")
    @ResponseBody
    public java.util.List<java.util.Map<String, Object>> obtenerPorProveedor(@PathVariable Integer id) {
        List<Combustible> lista = combustibleService.buscarPorProveedor(id);
        
        java.util.List<java.util.Map<String, Object>> respuesta = new java.util.ArrayList<>();
        
        for (Combustible c : lista) {
            java.util.Map<String, Object> mapa = new java.util.HashMap<>();
            mapa.put("idCombustible", c.getIdCombustible());

            mapa.put("nombre", c.getNombre() + " (S/ " + c.getPrecioActual() + ")");
            respuesta.add(mapa);
        }
        return respuesta;
    }
}