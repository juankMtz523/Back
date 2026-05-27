package com.gtim.service_orders.mapper;

import com.gtim.service_orders.dto.RolesPropuestaDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;

public class AssignResourcesRowMapper implements RowMapper<RolesPropuestaDTO> {
    @Override
    public RolesPropuestaDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
        RolesPropuestaDTO dto = new RolesPropuestaDTO();
        dto.setIdRolPropuesta(rs.getString("idproporsalrole") != null ? rs.getLong("idproporsalrole") : null);
        dto.setIdPropuesta(rs.getString("proposal_id") != null ? rs.getLong("proposal_id") : null);
        dto.setIdOrdenServicio(rs.getString("serviceorder_id") != null ? rs.getLong("serviceorder_id") : null);
        dto.setIdAsignacion(rs.getString("idasignacion") != null ? rs.getLong("idasignacion") : null);
        dto.setIdColaborador(rs.getString("idcolaborador") != null ? rs.getLong("idcolaborador") : null);
        dto.setNombreColaborador(rs.getString("nombrecolaborador") != null ? rs.getString("nombrecolaborador") : null);
        dto.setIdRol(rs.getString("role_id") != null ? rs.getLong("role_id") : null);
        dto.setNombreRol(rs.getString("role_description") != null ? rs.getString("role_description") : null);
        dto.setTipoDesarrollador(rs.getString("type_developer") != null ? rs.getString("type_developer") : null);
        dto.setCantidad(rs.getString("quantity") != null ? rs.getLong("quantity") : null);
        dto.setTipoAsingacion(rs.getString("assignment_type") != null ? rs.getString("assignment_type") : null);
        dto.setPorcentajeAsignacion(rs.getString("assignment_percentage") != null ? rs.getLong("assignment_percentage") : null);
        dto.setTiempoAsignacion(rs.getString("tiempo") != null ? rs.getLong("tiempo") : null);
        if(rs.getString("fechainicio") != null){
            LocalDate fechaIni = rs.getDate("fechainicio").toLocalDate();
            dto.setFechaInicioAsignacion(fechaIni);
            fechaIni = null;
        }else{
            dto.setFechaInicioAsignacion(null);
        }
        if(rs.getString("fechafin") != null){
            LocalDate fechaFin = rs.getDate("fechafin").toLocalDate();
            dto.setFechaFinAsignacion(fechaFin);
            fechaFin = null;
        }else{
            dto.setFechaFinAsignacion(null);
        }
        dto.setTotalHorasAsignacion(rs.getString("totaHoras") != null ? rs.getLong("totaHoras") : null);
        dto.setTotalHorasCumplidas(rs.getString("horascumplidas") != null ? rs.getLong("horascumplidas") : null);
        dto.setPctAsignacionTotal(rs.getString("pctasignacion") != null ? rs.getLong("pctasignacion") : null);
        dto.setFinalizado(rs.getString("finalizado") != null ? rs.getLong("finalizado") : null);
        dto.setIndiceRol(rs.getString("indiceRol") != null ? rs.getLong("indiceRol") : null);
        
        return dto;
    }
}
