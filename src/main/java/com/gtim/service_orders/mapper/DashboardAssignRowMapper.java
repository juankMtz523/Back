package com.gtim.service_orders.mapper;

import com.gtim.service_orders.dto.DashboardAssignmentDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class DashboardAssignRowMapper implements RowMapper<DashboardAssignmentDTO> {
    @Override
    public DashboardAssignmentDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
        DashboardAssignmentDTO dto = new DashboardAssignmentDTO();
        dto.setIdProyecto(rs.getString("id") != null ? rs.getLong("id") : null);
        dto.setProjectName(rs.getString("project_name") != null ? rs.getString("project_name") : null);
        dto.setInternalFolio(rs.getString("internal_folio") != null ? rs.getString("internal_folio") : null);
        dto.setTentativeStartDate(rs.getString("tentative_start_date") != null ? rs.getString("tentative_start_date") : null);
        dto.setTotalPropuesta(rs.getString("total_propuesta") != null ? rs.getLong("total_propuesta") : null);
        dto.setPorFinalizar(rs.getString("porfinalizar") != null ? rs.getLong("porfinalizar") : null);
        
        return dto;
    }
}
