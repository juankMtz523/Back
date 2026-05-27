package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;
import com.gtim.service_orders.dto.CatResourceDTO;
import com.gtim.service_orders.entity.CatResource;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;

import java.util.List;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatResourceMapper {

    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "area.description", target = "areaName")
    @Mapping(source = "rol.id", target = "roleId")
    @Mapping(source = "rol.description", target = "roleName")
    @Mapping(source = "coordinador.id", target = "coordinatorId")
    @Mapping(source = "coordinador.name", target = "coordinatorName")            
    @Mapping(source = "coordinador.email", target = "coordinatorEmail")
    CatResourceDTO toDto(CatResource entity);
    
    @Mapping(source = "areaId", target = "area.id")
    @Mapping(source = "areaName", target = "area.description")
    @Mapping(source = "roleId", target = "rol.id")
    @Mapping(source = "roleName", target = "rol.description")
    @Mapping(source = "coordinatorId", target = "coordinador.id")
    @Mapping(source = "coordinatorName", target = "coordinador.name")            
    @Mapping(source = "coordinatorEmail", target = "coordinador.email")    
    CatResource toEntity(CatResourceDTO dto);
    List<CatResourceDTO> toDtoList(List<CatResource> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CatResourceDTO dto, @MappingTarget CatResource entity);
}
