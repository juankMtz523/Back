package com.gtim.service_orders.mapper;

import com.gtim.service_orders.dto.ProyectosColaboradorDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ProyectosColaboradorRowMapper implements RowMapper<ProyectosColaboradorDTO> {
    @Override
    public ProyectosColaboradorDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
        ProyectosColaboradorDTO dto = new ProyectosColaboradorDTO();
        dto.setIdProyecto(rs.getString("idproyecto") != null ? rs.getLong("idproyecto") : null);
        dto.setNombreProyecto(rs.getString("nombreproyecto") != null ? rs.getString("nombreproyecto") : null);
        dto.setFolioInterno(rs.getString("foliointerno") != null ? rs.getString("foliointerno") : null);
        dto.setIdPropuesta(rs.getString("idpropuesta") != null ? rs.getLong("idpropuesta") : null);
        dto.setFolioPropuesta(rs.getString("foliopropuesta") != null ? rs.getString("foliopropuesta") : null);
        dto.setIdAsignacion(rs.getString("idasignacion") != null ? rs.getLong("idasignacion") : null);
        dto.setFechaInicio(rs.getString("fechainicio") != null ? rs.getString("fechainicio") : null);
        dto.setFechaFin(rs.getString("fechafinal") != null ? rs.getString("fechafinal") : null);
        dto.setTotalHoras(rs.getString("totalhoras") != null ? rs.getLong("totalhoras") : null);
        dto.setHorasCumplidas(rs.getString("horascumplidas") != null ? rs.getLong("horascumplidas") : null);
        
        return dto;
    }
}
