package com.gtim.service_orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dto.CatStageStatusDTO;
import com.gtim.service_orders.mapper.CatStageStatusMapper;
import com.gtim.service_orders.repository.CatStageStatusRepository;
import com.gtim.service_orders.service.CatStageStatusService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
public class CatStageStatusServiceImpl implements CatStageStatusService {

    private final CatStageStatusRepository repository;
    private final CatStageStatusMapper mapper;

    @Override
    public List<CatStageStatusDTO> getActiveStageStatus() {
        Sort sort = Sort.by(Sort.Direction.ASC,"name");
        return repository.findByActiveTrue(sort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
