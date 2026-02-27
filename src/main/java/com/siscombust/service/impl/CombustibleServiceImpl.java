package com.siscombust.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siscombust.entity.Combustible;
import com.siscombust.repository.CombustibleRepository;
import com.siscombust.service.CombustibleService;

@Service
public class CombustibleServiceImpl implements CombustibleService {
    @Autowired private CombustibleRepository repo;

    @Override 
    public List<Combustible> listarTodos() { return repo.findAll(); }
    @Override 
    public Combustible guardar(Combustible c) { return repo.save(c); }
    @Override 
    public Combustible buscarPorId(Integer id) { return repo.findById(id).orElse(null); }
    @Override 
    public void eliminar(Integer id) { repo.deleteById(id); }
    @Override
    public List<Combustible> buscarPorProveedor(Integer idProveedor) {
        return repo.findByProveedorIdProveedor(idProveedor);
    }
}