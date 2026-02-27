package com.siscombust.service;
import java.util.List;
import com.siscombust.entity.Combustible;

public interface CombustibleService {
    public List<Combustible> listarTodos();
    public Combustible guardar(Combustible combustible);
    public Combustible buscarPorId(Integer id);
    public void eliminar(Integer id);
    public List<Combustible> buscarPorProveedor(Integer idProveedor);
}