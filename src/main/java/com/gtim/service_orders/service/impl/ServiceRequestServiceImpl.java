package com.gtim.service_orders.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.dto.ProjectAutocompleteDTO;
import com.gtim.service_orders.dto.ProjectDashboardDTO;
import com.gtim.service_orders.dto.ServiceRequestDTO;
import com.gtim.service_orders.dto.ServiceRequestDetailDTO;
import com.gtim.service_orders.entity.CatArea;
import com.gtim.service_orders.entity.CatClient;
import com.gtim.service_orders.entity.CatProjectStatus;
import com.gtim.service_orders.entity.CatStageStatus;
import com.gtim.service_orders.entity.ServiceRequest;
import com.gtim.service_orders.exception.BusinessException;
import com.gtim.service_orders.exception.ResourceNotFoundException;
import com.gtim.service_orders.mapper.ServiceRequestDetailMapper;
import com.gtim.service_orders.mapper.ServiceRequestMapper;

import com.gtim.service_orders.repository.CatAreaRepository;
import com.gtim.service_orders.repository.CatClientRepository;
import com.gtim.service_orders.repository.CatProjectStatusRepository;
import com.gtim.service_orders.repository.ServiceRequestRepository;
import com.gtim.service_orders.repository.CommercialProposalRepository;
import com.gtim.service_orders.repository.CatStageStatusRepository;
import com.gtim.service_orders.service.ServiceRequestService;
import com.gtim.service_orders.service.CommercialProposalService;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final ServiceRequestMapper mapper;
    private final CatClientRepository catClientRepository;
    private final CatAreaRepository catAreaRepository;
    private final CatProjectStatusRepository catProjectStatusRepository;
    private final CatStageStatusRepository catStageStatusRepository;
    private final ServiceRequestDetailMapper serviceRequestDetailMapper ;
    private final CommercialProposalService commercialProposalService;

    @Override
    public ServiceRequestDTO create(ServiceRequestDTO dto, String currentUser) {
    	
    	  boolean exists = serviceRequestRepository
    		        .existsByProjectNameIgnoreCaseAndClientIdAndAreaIdAndActiveTrue(
    		            dto.getProjectName(),
    		            dto.getClientId(),
    		            dto.getAreaId()
    		        );

    		    if (exists) {
    		        throw new BusinessException(
    		            "Ya existe un proyecto activo con el mismo nombre para este cliente y área"
    		        );
    		    }
    		    
        ServiceRequest entity = mapper.toEntity(dto);
        
        if (dto.getGeneralStatusId() != null) {
            CatProjectStatus status = catProjectStatusRepository.findById(dto.getGeneralStatusId())
                    .orElseThrow(() -> new BusinessException("Estatus general no válido"));

            entity.setGeneralStatus(status);
        }else{
            entity.setGeneralStatus(null);
        }
        
        if(dto.getMaturationStatusId() != null){
            CatStageStatus maturation = catStageStatusRepository.findById(dto.getMaturationStatusId())
                    .orElseThrow(() -> new BusinessException("Estatus Maduración no válido"));
            
            entity.setMaturationStatus(maturation);
        }else{
            entity.setMaturationStatus(null);
        }

        if(dto.getConstructionStatusId() != null){
            CatStageStatus constrution = catStageStatusRepository.findById(dto.getConstructionStatusId())
                    .orElseThrow(() -> new BusinessException("Estatus Construcción no válido"));
            
            entity.setConstructionStatus(constrution);
        }else{
            entity.setConstructionStatus(null);
        }

        if(dto.getStabilizationStatusId() != null){
            CatStageStatus stabilitation = catStageStatusRepository.findById(dto.getStabilizationStatusId())
                    .orElseThrow(() -> new BusinessException("Estatus Estabilización no válido"));
            
            entity.setStabilizationStatus(stabilitation);
        }else{
            entity.setStabilizationStatus(null);
        }
        
        entity.setInternalFolio(generarInternalFolio(dto.getClientId(), dto.getAreaId()));

        entity.setActive(true);

        entity.setCreatedBy(currentUser);
        entity.setCreatedAt(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        
        entity.setCoordinator(null);
        return mapper.toDto(serviceRequestRepository.save(entity));
    }

    @Override
    public ServiceRequestDTO update(Long id, ServiceRequestDTO dto, String currentUser) {
        
        ServiceRequest entity = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Requerimiento no encontrado"));

        mapper.updateEntityFromDto(dto, entity);

        entity.setUpdatedBy(currentUser);
        entity.setUpdatedAt(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        
        if (dto.getGeneralStatusId() != null) {
            CatProjectStatus status = catProjectStatusRepository.findById(dto.getGeneralStatusId())
                    .orElseThrow(() -> new BusinessException("Estatus general no válido"));

            entity.setGeneralStatus(status);
        }
        
        if(dto.getMaturationStatusId() != null){
            CatStageStatus maturation = catStageStatusRepository.findById(dto.getMaturationStatusId())
                    .orElseThrow(() -> new BusinessException("Estatus Maduración no válido"));
            
            entity.setMaturationStatus(maturation);
        }

        if(dto.getConstructionStatusId() != null){
            CatStageStatus constrution = catStageStatusRepository.findById(dto.getConstructionStatusId())
                    .orElseThrow(() -> new BusinessException("Estatus Construcción no válido"));
            
            entity.setConstructionStatus(constrution);
        }

        if(dto.getStabilizationStatusId() != null){
            CatStageStatus stabilitation = catStageStatusRepository.findById(dto.getStabilizationStatusId())
                    .orElseThrow(() -> new BusinessException("Estatus Estabilización no válido"));
            
            entity.setStabilizationStatus(stabilitation);
        }
        
        return mapper.toDto(serviceRequestRepository.save(entity));
    }

    @Override
    public ServiceRequestDTO findById(Long id) {
        return serviceRequestRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new BusinessException("Requerimiento no encontrado"));
    }

    @Override
    public List<ServiceRequestDTO> findAll(String currenUser) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        List<ServiceRequestDTO> listadoNew = new ArrayList<>();
        ServiceRequestDTO srdto;
        Pageable paging = PageRequest.of(0, 100);

        List<ServiceRequestDTO> listado = serviceRequestRepository.findByActiveTrueAndCreatedBy(currenUser, sort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        
        /*List<ServiceRequestDTO> listado = serviceRequestRepository.findAll(sort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());*/
        
        for(ServiceRequestDTO s: listado){
            srdto = s;
            srdto.setListProposal(commercialProposalService.findByProjectId(s.getId(), paging).getContent());
            listadoNew.add(srdto);
        }
        return listadoNew;
    }

    @Override
    public void delete(Long id) {
        ServiceRequest entity = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Requerimiento no encontrado"));
        serviceRequestRepository.delete(entity);
    }

    // Generar folio interno GTIM
    private String generarInternalFolio(Long clientId, Long areaId) {
        CatClient cliente = catClientRepository.findById(clientId)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        CatArea area = catAreaRepository.findById(areaId)
                .orElseThrow(() -> new BusinessException("Área no encontrada"));

        String prefix = cliente.getShortName() + "_" + area.getName() + "_";

        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        
        Optional<ServiceRequest> lastFolio = serviceRequestRepository
                .findTopByInternalFolioStartingWithOrderByIdDesc(prefix, sort);

        int nextNumber = 1;
        if (lastFolio.isPresent()) {
            String[] parts = lastFolio.get().getInternalFolio().split("_");
            try {
                nextNumber = Integer.parseInt(parts[2]) + 1;
            } catch (NumberFormatException e) {
                nextNumber = 1;
            }
        }

        return prefix + String.format("%03d", nextNumber);
    }
    
    @Override
    public List<ProjectAutocompleteDTO> autocompleteProjects(String query) {

        if (query == null || query.trim().length() < 2) {
            return List.of();
        }

        return serviceRequestRepository.findByProjectNameOrInternalFolio(query.trim());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDashboardDTO> getDashboard(Pageable pageable) {
        return serviceRequestRepository.findProjectsForDashboard(pageable);
    }
    
    @Override
    public ServiceRequestDetailDTO findDetailById(Long id) {
        ServiceRequest entity = serviceRequestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "ServiceRequest", "id", id
            ));

        return serviceRequestDetailMapper.toDto(entity);
    }



}
