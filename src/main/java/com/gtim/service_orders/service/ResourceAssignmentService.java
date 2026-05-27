package com.gtim.service_orders.service;

import java.util.List;

import com.gtim.service_orders.dto.DashboardAssignmentDTO;
import com.gtim.service_orders.dto.ProyectoPropuestasDTO;
import com.gtim.service_orders.dto.ResourceAssignmentDTO;

public interface ResourceAssignmentService {

    /*ResourceAssignmentDTO assign(ResourceAssignmentDTO dto);
    List<ResourceAssignmentDTO> findByServiceOrder(Long serviceOrderId);*/

    List<DashboardAssignmentDTO> getDashboard(Long estatus, String username);

    List<ProyectoPropuestasDTO> getPropuestasXProyecto(Long proyectoId);

    public ResourceAssignmentDTO create(ResourceAssignmentDTO request, String user);

    public ResourceAssignmentDTO update(ResourceAssignmentDTO request, String user);
}
