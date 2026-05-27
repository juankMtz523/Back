package com.gtim.service_orders.mapper;

import com.gtim.service_orders.dto.CorreoCoordinadorDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CorreoCoordinadorRowMapper implements RowMapper<CorreoCoordinadorDTO>{
    @Override
    public CorreoCoordinadorDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
        CorreoCoordinadorDTO dto = new CorreoCoordinadorDTO();
        dto.setNombreCoordinador(rs.getString("nombre") != null ? rs.getString("nombre") : null);
        dto.setCorreoCoordinador(rs.getString("correo") != null ? rs.getString("correo") : null);
        
        return dto;
    }
    
}
