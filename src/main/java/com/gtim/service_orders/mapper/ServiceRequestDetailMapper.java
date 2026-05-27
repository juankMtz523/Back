package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gtim.service_orders.dto.ServiceRequestDetailDTO;
import com.gtim.service_orders.entity.ServiceRequest;

@Mapper(componentModel = "spring")
public interface ServiceRequestDetailMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")

    @Mapping(source = "area.id", target = "areaId")
    @Mapping(source = "area.name", target = "areaName")

    @Mapping(source = "coordinator.id", target = "coordinatorId")
    @Mapping(source = "coordinator.name", target = "coordinatorName")

    @Mapping(source = "generalStatus.id", target = "generalStatusId")
    @Mapping(source = "generalStatus.name", target = "generalStatusName")

    @Mapping(source = "maturationStatus.id", target = "maturationStatusId")
    @Mapping(source = "maturationStatus.name", target = "maturationStatusName")

    @Mapping(source = "constructionStatus.id", target = "constructionStatusId")
    @Mapping(source = "constructionStatus.name", target = "constructionStatusName")

    @Mapping(source = "stabilizationStatus.id", target = "stabilizationStatusId")
    @Mapping(source = "stabilizationStatus.name", target = "stabilizationStatusName")

    ServiceRequestDetailDTO toDto(ServiceRequest entity);
}

