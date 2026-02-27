package com.siscombust.service;

import java.util.List;
import com.siscombust.entity.Vehiculo;

public interface VehiculoService {
    public List<Vehiculo> listarTodos();
    
    public Vehiculo guardar(Vehiculo vehiculo);
    
    public Vehiculo buscarPorId(Integer id);
    
    public void eliminar(Integer id);
    
    public List<Vehiculo> buscarPorMarca(String marca);
}