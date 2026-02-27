package com.siscombust.service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

    public byte[] generarReporteConsumos(List<?> consumos) throws Exception {

        File file = ResourceUtils.getFile("classpath:reportes/consumos.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(consumos);
        Map<String, Object> parametros = new HashMap<>(); 

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    public byte[] generarReporteProveedores(List<?> proveedores) throws Exception {
        File file = ResourceUtils.getFile("classpath:reportes/proveedores.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proveedores);
        
        Map<String, Object> parametros = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    

    public byte[] generarReporteVehiculos(java.util.List<com.siscombust.entity.Vehiculo> vehiculos) throws Exception {

        java.io.InputStream reportStream = getClass().getResourceAsStream("/reportes/vehiculos.jrxml");
        
       JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(vehiculos);
        Map<String, Object> parameters = new java.util.HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}