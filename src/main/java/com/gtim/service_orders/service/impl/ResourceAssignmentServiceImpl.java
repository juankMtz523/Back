package com.gtim.service_orders.service.impl;

import com.gtim.service_orders.dto.DashboardAssignmentDTO;
import com.gtim.service_orders.dto.ProyectoPropuestasDTO;
import com.gtim.service_orders.dto.ResourceAssignmentDTO;
import com.gtim.service_orders.entity.TrxResourceAssignment;
import com.gtim.service_orders.entity.CommercialProposalRole;
import com.gtim.service_orders.mapper.TrxResourceAssignmentMapper;
import com.gtim.service_orders.repository.ResourceAssignmentRepository;
import com.gtim.service_orders.repository.CommercialProposalRoleRepository;
import com.gtim.service_orders.service.ResourceAssignmentService;
import com.gtim.service_orders.dao.ResourceAssignmentDAO;
import com.gtim.service_orders.exception.BusinessException;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ResourceAssignmentServiceImpl implements ResourceAssignmentService {

    @Autowired
    private ResourceAssignmentRepository repository;
    @Autowired
    private CommercialProposalRoleRepository roleRepository;
    @Autowired
    private ResourceAssignmentDAO dao;
    @Autowired
    private TrxResourceAssignmentMapper mapper;

    @Override
    public List<DashboardAssignmentDTO> getDashboard(Long estatus, String username) {
        List<DashboardAssignmentDTO> listado = new ArrayList<>();
        listado = dao.getDashboard(estatus, username);
        return listado;
    }

    @Override
    public List<ProyectoPropuestasDTO> getPropuestasXProyecto(Long proyectoId) {
        List<ProyectoPropuestasDTO> propuestas = new ArrayList<>();
        ProyectoPropuestasDTO propuesta;

        for (ProyectoPropuestasDTO pp : dao.getProjectProposals(proyectoId)) {
            propuesta = pp;
            propuesta.setListadoRolesAsignacion(dao.getRolesProposal(pp.getPropuestaId()));
            propuestas.add(pp);
        }

        return propuestas;
    }

    @Override
    public ResourceAssignmentDTO create(ResourceAssignmentDTO request, String user) {

        CommercialProposalRole cpr = roleRepository.findById(request.getProposalRoleId())
                .orElseThrow(() -> new BusinessException("Rol no encontrado en la propuesta"));
        
        TrxResourceAssignment entity = mapper.toEntity(request);
        //LocalDate fechaFinal = repository.callFunctionFechaFinal(request.getStartDate(), request.getTotalHoras());
        
        String p_tipoasignacion = cpr.getAssignmentType();
        Long p_porcentajeasignacion = cpr.getAssignmentPercentage().longValue();
        Long p_cantidadasignacion = cpr.getMonths().longValue();
        LocalDate p_fechainicio = request.getStartDate();
        
        LocalDate fechaFinal = repository.callFunctionFechaFinal2(p_tipoasignacion, p_porcentajeasignacion, p_cantidadasignacion, p_fechainicio);
        entity.setEndDate(fechaFinal);
        entity.setCreatedBy(user);
        entity.setCreatedAt(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        entity.setActive(true);

        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ResourceAssignmentDTO update(ResourceAssignmentDTO request, String user) {

        TrxResourceAssignment entityOld = repository.findById(request.getIdResourceAssignment())
                .orElseThrow(() -> new BusinessException("ASignación no encontrada"));

        TrxResourceAssignment entity = mapper.toEntity(request);
        entity.setCreatedBy(entityOld.getCreatedBy());
        entity.setCreatedAt(entityOld.getCreatedAt());
        entity.setUpdatedBy(user);
        entity.setUpdatedAt(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        entity.setActive(true);

        return mapper.toDto(repository.save(entity));
    }

}
