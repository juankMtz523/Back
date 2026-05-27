package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;

import com.gtim.service_orders.dto.ServiceRequestDTO;
import com.gtim.service_orders.entity.ServiceRequest;

import java.util.List;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceRequestMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "coordinator.id", target = "coordinatorId")
    @Mapping(source = "maturationStatus.id", target = "maturationStatusId")
    @Mapping(source = "constructionStatus.id", target = "constructionStatusId")
    @Mapping(source = "stabilizationStatus.id", target = "stabilizationStatusId")
    @Mapping(source = "generalStatus.id", target = "generalStatusId")
    ServiceRequestDTO toDto(ServiceRequest entity);

    @Mapping(source = "clientId", target = "client.id")
    @Mapping(source = "areaId", target = "area.id")
    @Mapping(source = "coordinatorId", target = "coordinator.id")
    @Mapping(source = "maturationStatusId", target = "maturationStatus.id")
    @Mapping(source = "constructionStatusId", target = "constructionStatus.id")
    @Mapping(source = "stabilizationStatusId", target = "stabilizationStatus.id")
    ServiceRequest toEntity(ServiceRequestDTO dto);

    List<ServiceRequestDTO> toDtoList(List<ServiceRequest> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ServiceRequestDTO dto, @MappingTarget ServiceRequest entity);
}
