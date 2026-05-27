package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;

import com.gtim.service_orders.dto.CatRoleDTO;
import com.gtim.service_orders.entity.CatRole;

@Mapper(componentModel = "spring")
public interface CatRoleMapper {
    CatRoleDTO toDto(CatRole entity);
    CatRole toEntity(CatRoleDTO dto);
}

