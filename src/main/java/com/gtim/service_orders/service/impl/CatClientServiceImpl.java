package com.gtim.service_orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dto.CatClientDTO;
import com.gtim.service_orders.mapper.CatClientMapper;
import com.gtim.service_orders.repository.CatClientRepository;
import com.gtim.service_orders.service.CatClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
public class CatClientServiceImpl implements CatClientService {

    private final CatClientRepository repository;
    private final CatClientMapper mapper;

    @Override
    public List<CatClientDTO> getActiveClients() {
        Sort sort = Sort.by(Sort.Direction.ASC,"name");
        return repository.findByActiveTrue(sort)
                         .stream()
                         .map(mapper::toDto)
                         .collect(Collectors.toList());
    }
}
