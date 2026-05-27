package com.gtim.service_orders.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dto.CatProjectStatusDTO;
import com.gtim.service_orders.mapper.CatProjectStatusMapper;
import com.gtim.service_orders.repository.CatProjectStatusRepository;
import com.gtim.service_orders.service.CatProjectStatusService;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
public class CatProjectStatusServiceImpl implements CatProjectStatusService {

    private final CatProjectStatusRepository repository;
    private final CatProjectStatusMapper mapper;

    @Override
    public List<CatProjectStatusDTO> getActiveStatuses() {
        Sort sort = Sort.by(Sort.Direction.ASC,"name");
        return repository.findByActiveTrue(sort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
