package com.siscombust.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siscombust.entity.Vehiculo;
import com.siscombust.repository.VehiculoRepository;
import com.siscombust.service.VehiculoService;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    @Autowired
    private VehiculoRepository repositorio;

    @Override
    public List<Vehiculo> listarTodos() {
        return repositorio.findAll();
    }

    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {
        return repositorio.save(vehiculo);
    }

    @Override
    public Vehiculo buscarPorId(Integer id) {
        return repositorio.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
    
    @Override
    public List<Vehiculo> buscarPorMarca(String marca) {
        return repositorio.findByMarcaContainingIgnoreCase(marca);
    }
}