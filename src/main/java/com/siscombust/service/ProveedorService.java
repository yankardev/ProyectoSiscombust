package com.siscombust.service;

import java.util.List;
import com.siscombust.entity.Proveedor;

public interface ProveedorService {
    public List<Proveedor> listarTodos();
    public Proveedor guardar(Proveedor proveedor);
    public Proveedor buscarPorId(Integer id);
    public void eliminar(Integer id);
}