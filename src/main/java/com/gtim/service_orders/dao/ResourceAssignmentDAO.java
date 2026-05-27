package com.gtim.service_orders.dao;

import com.gtim.service_orders.dto.DashboardAssignmentDTO;
import com.gtim.service_orders.dto.ProyectoPropuestasDTO;
import com.gtim.service_orders.dto.RolesPropuestaDTO;
import java.util.List;

public interface ResourceAssignmentDAO {

    public List<DashboardAssignmentDTO> getDashboard(Long estatus, String username);

    public List<ProyectoPropuestasDTO> getProjectProposals(Long idProject);

    public List<RolesPropuestaDTO> getRolesProposal(Long idProposal);
}
