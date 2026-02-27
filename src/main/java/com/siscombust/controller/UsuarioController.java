package com.siscombust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.siscombust.entity.Usuario;
import com.siscombust.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuario/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        return "usuario/nuevo";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuario") com.siscombust.entity.Usuario usuario, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttrs) {
        try {
            
            // Aquí va tu código normal para guardar (quizás tengas algo de encriptar contraseña antes)
            usuarioService.guardar(usuario);
            
            redirectAttrs.addFlashAttribute("mensajeExito", "Usuario registrado o actualizado correctamente.");
            
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Aquí atrapamos el error de la base de datos si el nombreUsuario ya existe
            redirectAttrs.addFlashAttribute("mensajeError", 
                "¡ERROR! El usuario (login) '" + usuario.getNombreUsuario() + "' ya está registrado. Por favor elige otro.");
        }
        
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("usuario", usuarioService.buscarPorId(id));
        return "usuario/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
    	usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}