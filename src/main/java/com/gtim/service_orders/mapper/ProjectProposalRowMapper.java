package com.gtim.service_orders.mapper;

import com.gtim.service_orders.dto.ProyectoPropuestasDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import org.springframework.jdbc.core.RowMapper;

public class ProjectProposalRowMapper implements RowMapper<ProyectoPropuestasDTO> {

    @Override
    public ProyectoPropuestasDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProyectoPropuestasDTO dto = new ProyectoPropuestasDTO();
        dto.setProyectoId(rs.getString("idproject") != null ? rs.getLong("idproject") : null);
        dto.setPropuestaId(rs.getString("idproposal") != null ? rs.getLong("idproposal") : null);
        dto.setServiceOrderId(rs.getString("idserviceorder") != null ? rs.getLong("idserviceorder") : null);
        dto.setNombreProyecto(rs.getString("project_name") != null ? rs.getString("project_name") : null);
        dto.setFolioCliente(rs.getString("cliente_folio") != null ? rs.getString("cliente_folio") : null);
        dto.setFolioInterno(rs.getString("internal_folio") != null ? rs.getString("internal_folio") : null);
        dto.setFechaInicioProyecto(rs.getString("fechainicio") != null ? rs.getDate("fechainicio") : null);
        dto.setFolioPropuesta(rs.getString("folioproposal") != null ? rs.getString("folioproposal") : null);
        dto.setFolioOrdenServicio(rs.getString("serviceorderfolio") != null ? rs.getString("serviceorderfolio") : null);
        dto.setFechaOrdenservicio(rs.getString("fechacreacion") != null ? rs.getDate("fechacreacion") : null);
        dto.setListadoRolesAsignacion(null);
        return dto;
    }
}
