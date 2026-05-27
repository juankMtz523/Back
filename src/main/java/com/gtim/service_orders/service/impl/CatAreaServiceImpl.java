package com.gtim.service_orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dto.CatAreaDTO;
import com.gtim.service_orders.mapper.CatAreaMapper;
import com.gtim.service_orders.repository.CatAreaRepository;
import com.gtim.service_orders.service.CatAreaService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
public class CatAreaServiceImpl implements CatAreaService {

    private final CatAreaRepository repository;
    private final CatAreaMapper mapper;

    @Override
    public List<CatAreaDTO> getActiveAreas(Long tipoArea) {
        Sort sort = Sort.by(Sort.Direction.ASC,"description");
        return repository.findByTipoAreaId(tipoArea, sort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
