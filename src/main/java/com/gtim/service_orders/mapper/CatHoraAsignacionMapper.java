package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;

import com.gtim.service_orders.dto.CatHoraAsignacionDTO;
import com.gtim.service_orders.entity.CatHorasasginacion;

@Mapper(componentModel = "spring")
public interface CatHoraAsignacionMapper {
    CatHoraAsignacionDTO toDto(CatHorasasginacion entity);
    CatHorasasginacion toEntity(CatHoraAsignacionDTO dto);    
}
