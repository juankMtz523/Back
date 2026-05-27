package com.gtim.service_orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dto.CatRoleDTO;
import com.gtim.service_orders.mapper.CatRoleMapper;
import com.gtim.service_orders.repository.CatRoleRepository;
import com.gtim.service_orders.service.CatRoleService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
public class CatRoleServiceImpl implements CatRoleService {

    private final CatRoleRepository repository;
    private final CatRoleMapper mapper;

    @Override
    public List<CatRoleDTO> getActiveRoles() {
        Sort sort = Sort.by(Sort.Direction.ASC,"description");
        return repository.findByRolSystemFalse(sort).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
