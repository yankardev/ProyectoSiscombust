package com.siscombust.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.siscombust.service.impl.ReporteService;
import com.siscombust.entity.Combustible;
import com.siscombust.entity.Consumo;
import com.siscombust.entity.Proveedor;
import com.siscombust.entity.Vehiculo;
import com.siscombust.service.CombustibleService;
import com.siscombust.service.ConsumoService;
import com.siscombust.service.ProveedorService;
import com.siscombust.service.VehiculoService;

@Controller
@RequestMapping("/consumos")
public class ConsumoController {

    @Autowired 
    private ConsumoService consumoService;
    @Autowired 
    private VehiculoService vehiculoService;
    @Autowired 
    private ProveedorService proveedorService;
    @Autowired 
    private CombustibleService combustibleService;
    @Autowired 
    private ReporteService reporteService;

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("consumos", consumoService.listarTodos());
        return "consumo/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        modelo.addAttribute("consumo", new Consumo());
        modelo.addAttribute("vehiculos", vehiculoService.listarTodos());
        modelo.addAttribute("proveedores", proveedorService.listarTodos());
        modelo.addAttribute("combustibles", combustibleService.listarTodos());
        return "consumo/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("consumo") Consumo consumo, RedirectAttributes redirectAttrs) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (consumo.getUsuarioRegistro() == null || consumo.getUsuarioRegistro().isEmpty()) {
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                consumo.setUsuarioRegistro(auth.getName());
            } else {
                consumo.setUsuarioRegistro("Sistema");
            }
        }
        Vehiculo vehiculoBd = vehiculoService.buscarPorId(consumo.getVehiculo().getIdVehiculo());
        
        if (vehiculoBd.getEstado() != null && !vehiculoBd.getEstado().equals("Activo")) {
            redirectAttrs.addFlashAttribute("mensajeError", 
                "¡ALERTA DE SEGURIDAD! El vehículo " + vehiculoBd.getPlaca() + 
                " figura como '" + vehiculoBd.getEstado() + "'. No está autorizado para cargar combustible.");
            return "redirect:/consumos"; 
        }

        BigDecimal capacidadTanque = new BigDecimal(vehiculoBd.getCapacidadGalones().toString());
        
        if (consumo.getCantidadGalones().compareTo(capacidadTanque) > 0) {
            redirectAttrs.addFlashAttribute("mensajeError", 
                "ERROR: El tanque del " + vehiculoBd.getPlaca() + " solo soporta " 
                + vehiculoBd.getCapacidadGalones() + " galones. Has excedido el límite.");
            return "redirect:/consumos"; 
        }
        
       Combustible combustibleBd = combustibleService.buscarPorId(consumo.getCombustible().getIdCombustible());
       BigDecimal totalCongelado = consumo.getCantidadGalones().multiply(combustibleBd.getPrecioActual());
        
      Proveedor proveedorBd = proveedorService.buscarPorId(consumo.getProveedor().getIdProveedor());
        
        if (proveedorBd.getLineaCredito() == null) {
            proveedorBd.setLineaCredito(BigDecimal.ZERO);
        }
        
        if (proveedorBd.getLineaCredito().compareTo(totalCongelado) < 0) {
            redirectAttrs.addFlashAttribute("mensajeError", 
                "CRÉDITO INSUFICIENTE: " + proveedorBd.getNombreProveedor() + 
                " solo tiene S/ " + proveedorBd.getLineaCredito() + " disponible. El consumo requiere S/ " + totalCongelado);
            return "redirect:/consumos";
        }

        BigDecimal nuevoSaldo = proveedorBd.getLineaCredito().subtract(totalCongelado);
        proveedorBd.setLineaCredito(nuevoSaldo);
        proveedorService.guardar(proveedorBd);

        consumo.setTotalSoles(totalCongelado);
        consumoService.guardar(consumo);
        
        redirectAttrs.addFlashAttribute("mensajeExito", "Abastecimiento registrado. Se descontaron S/ " + totalCongelado + " del crédito del proveedor.");
        return "redirect:/consumos";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("consumo", consumoService.buscarPorId(id));
        modelo.addAttribute("vehiculos", vehiculoService.listarTodos());
        modelo.addAttribute("proveedores", proveedorService.listarTodos());
        modelo.addAttribute("combustibles", combustibleService.listarTodos());
        return "consumo/editar";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        com.siscombust.entity.Consumo consumo = consumoService.buscarPorId(id);
        
        if (consumo != null) {
            Proveedor proveedor = consumo.getProveedor();
            BigDecimal saldoRestaurado = proveedor.getLineaCredito().add(consumo.getTotalSoles());
            
            proveedor.setLineaCredito(saldoRestaurado);
            proveedorService.guardar(proveedor); 

            consumoService.eliminar(id);
            
            redirectAttrs.addFlashAttribute("mensajeExito", 
                "Registro eliminado. Se han devuelto S/ " + consumo.getTotalSoles() + 
                " al crédito de " + proveedor.getNombreProveedor());
        }
        return "redirect:/consumos";
    }
    
    @GetMapping("/reporte")
    public ResponseEntity<byte[]> descargarReporte() throws Exception {
        List<Consumo> listaConsumos = consumoService.listarTodos();
        byte[] pdf = reporteService.generarReporteConsumos(listaConsumos);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Reporte_Consumos.pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}