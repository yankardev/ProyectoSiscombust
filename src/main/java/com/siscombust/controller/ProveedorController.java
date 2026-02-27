package com.siscombust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.siscombust.service.impl.ReporteService;

import com.siscombust.entity.Proveedor;
import com.siscombust.service.ProveedorService;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;
   
    
    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public String listarProveedores(Model modelo) {
        modelo.addAttribute("proveedores", proveedorService.listarTodos());
        return "proveedor/lista"; 
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistrarProveedor(Model modelo) {
        Proveedor proveedor = new Proveedor();
        modelo.addAttribute("proveedor", proveedor);
        return "proveedor/nuevo"; 
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("proveedor") com.siscombust.entity.Proveedor proveedor, RedirectAttributes redirectAttrs) {
        
    	List<com.siscombust.entity.Proveedor> todosLosProveedores = proveedorService.listarTodos();
        
        for (com.siscombust.entity.Proveedor p : todosLosProveedores) {

            if (p.getRuc().equals(proveedor.getRuc()) && !p.getIdProveedor().equals(proveedor.getIdProveedor())) {
   
                redirectAttrs.addFlashAttribute("mensajeError", 
                    "¡ALERTA DE DUPLICADO! Ya existe un proveedor registrado con el RUC: " + proveedor.getRuc() + 
                    " (" + p.getNombreProveedor() + "). Verifica los datos ingresados.");
                
                return "redirect:/proveedores"; 
            }
        }

        proveedorService.guardar(proveedor);
        redirectAttrs.addFlashAttribute("mensajeExito", "Proveedor registrado/actualizado correctamente en el sistema.");
        
        return "redirect:/proveedores";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditar(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("proveedor", proveedorService.buscarPorId(id));
        return "proveedor/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id) {
    	proveedorService.eliminar(id);
        return "redirect:/proveedores";
    }
    
    @GetMapping("/reporte")
    public ResponseEntity<byte[]> descargarReporte() throws Exception {
        List<Proveedor> listaProveedores = proveedorService.listarTodos();
        byte[] pdf = reporteService.generarReporteProveedores(listaProveedores);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Directorio_Proveedores.pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
    
    @PostMapping("/recargar")
    public String recargarSaldo(@RequestParam("idProveedor") Integer idProveedor, 
                                @RequestParam("monto") BigDecimal monto, 
                                RedirectAttributes redirectAttrs) {
        
        com.siscombust.entity.Proveedor proveedor = proveedorService.buscarPorId(idProveedor);
        
        if (proveedor != null && monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
  
            BigDecimal saldoActual = proveedor.getLineaCredito() != null ? proveedor.getLineaCredito() : BigDecimal.ZERO;
            
            BigDecimal nuevoSaldo = saldoActual.add(monto);
            proveedor.setLineaCredito(nuevoSaldo);
            
            proveedorService.guardar(proveedor);
            
            redirectAttrs.addFlashAttribute("mensajeExito", 
                "¡Recarga exitosa! Se han abonado S/ " + monto + " a la cuenta de " + proveedor.getNombreProveedor() + 
                ". Nuevo saldo: S/ " + nuevoSaldo);
        } else {
            redirectAttrs.addFlashAttribute("mensajeError", "Error: El monto ingresado no es válido.");
        }
        
        return "redirect:/proveedores";
    }
    
}