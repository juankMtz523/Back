package com.gtim.service_orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dto.CatProposalStatusDTO;
import com.gtim.service_orders.mapper.CatProposalStatusMapper;
import com.gtim.service_orders.repository.CatProposalStatusRepository;
import com.gtim.service_orders.service.CatProposalStatusService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
public class CatProposalStatusServiceImpl implements CatProposalStatusService {

    private final CatProposalStatusRepository repository;
    private final CatProposalStatusMapper mapper;

    @Override
    public List<CatProposalStatusDTO> getActiveProposalStatuses() {
        Sort sort = Sort.by(Sort.Direction.ASC,"name");
        return repository.findByActiveTrue(sort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
