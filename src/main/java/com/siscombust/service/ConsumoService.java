package com.siscombust.service;

import java.time.LocalDate;
import java.util.List;
import com.siscombust.entity.Consumo;

public interface ConsumoService {
	
    public List<Consumo> listarTodos();
    
    public Consumo guardar(Consumo consumo);
    
    public Consumo buscarPorId(Integer id);
    
    public void eliminar(Integer id);
    
    public List<Consumo> buscarPorFechas(LocalDate inicio, LocalDate fin);
}