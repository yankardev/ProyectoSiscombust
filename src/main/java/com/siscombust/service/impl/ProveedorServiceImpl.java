package com.siscombust.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siscombust.entity.Proveedor;
import com.siscombust.repository.ProveedorRepository;
import com.siscombust.service.ProveedorService;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository repositorio;

    @Override
    public List<Proveedor> listarTodos() {
        return repositorio.findAll();
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        return repositorio.save(proveedor);
    }
    
    @Override
    public Proveedor buscarPorId(Integer id) {
 
        return repositorio.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
}