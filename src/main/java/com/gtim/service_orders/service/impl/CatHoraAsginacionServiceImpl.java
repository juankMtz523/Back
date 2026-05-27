package com.gtim.service_orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dto.CatHoraAsignacionDTO;
import com.gtim.service_orders.mapper.CatHoraAsignacionMapper;
import com.gtim.service_orders.repository.CatHoraAsignacionRepository;
import com.gtim.service_orders.service.CatHoraAsginacionService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
public class CatHoraAsginacionServiceImpl implements CatHoraAsginacionService {
    
    private final CatHoraAsignacionRepository repository;
    private final CatHoraAsignacionMapper mapper;
    
    @Override
    public List<CatHoraAsignacionDTO> getHorasAsignacion(){
        Sort multiSort = Sort.by("porcentaje").descending().and(Sort.by("tipoAsignacion").ascending());
        return repository.findAll(multiSort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
