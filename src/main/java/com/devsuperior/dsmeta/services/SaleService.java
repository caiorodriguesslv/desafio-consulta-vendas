package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    public SaleMinDTO findById(Long id) {
        Sale entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
        return new SaleMinDTO(entity);
    }

    public Page<SaleReportDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {
        try {
            LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
            LocalDate max = (maxDate == null || maxDate.trim().isEmpty()) 
                ? today 
                : LocalDate.parse(maxDate.trim());
                
            LocalDate min = (minDate == null || minDate.trim().isEmpty()) 
                ? max.minusYears(1) 
                : LocalDate.parse(minDate.trim());
            
            if (min.isAfter(max)) {
                throw new IllegalArgumentException("Data inicial não pode ser maior que a data final");
            }
            
            name = (name == null) ? "" : name.trim();
            
            return repository.getReport(min, max, name, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar parâmetros: " + e.getMessage(), e);
        }
    }

    public Page<SaleSummaryDTO> getSummary(String minDate, String maxDate, Pageable pageable) {
        try {
            LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
            LocalDate max = (maxDate == null || maxDate.trim().isEmpty()) 
                ? today 
                : LocalDate.parse(maxDate.trim());
                
            LocalDate min = (minDate == null || minDate.trim().isEmpty()) 
                ? max.minusYears(1) 
                : LocalDate.parse(minDate.trim());
            
            if (min.isAfter(max)) {
                throw new IllegalArgumentException("Data inicial não pode ser maior que a data final");
            }
            
            return repository.getSummary(min, max, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar datas: " + e.getMessage(), e);
        }
    }
}
