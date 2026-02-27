package com.siscombust.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import com.siscombust.entity.Consumo;
import com.siscombust.repository.ConsumoRepository;
import com.siscombust.service.ConsumoService;

@Service
public class ConsumoServiceImpl implements ConsumoService {
    
    @Autowired 
    private ConsumoRepository consumoRepo;
    
    @Override 
    @Transactional(readOnly = true)
    public List<Consumo> listarTodos() { 
        return consumoRepo.findAll(); 
    }
    
    @Override 
    @Transactional 
    public Consumo guardar(Consumo c) { 
        return consumoRepo.save(c); 
    }
    
    @Override 
    @Transactional(readOnly = true)
    public Consumo buscarPorId(Integer id) { 
        return consumoRepo.findById(id).orElse(null); 
    }
    
    @Override 
    @Transactional
    public void eliminar(Integer id) { 
    	consumoRepo.deleteById(id); 
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Consumo> buscarPorFechas(LocalDate inicio, LocalDate fin) {
        return consumoRepo.findByFechaConsumoBetween(inicio, fin);
    }
}