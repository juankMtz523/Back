package com.gtim.service_orders.mapper;

import com.gtim.service_orders.dto.ColaboradoresCoordinadorDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ColaboradoresCoordinadorRowMapper implements RowMapper<ColaboradoresCoordinadorDTO> {
    @Override
    public ColaboradoresCoordinadorDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
        ColaboradoresCoordinadorDTO dto = new ColaboradoresCoordinadorDTO();
        dto.setId(rs.getString("id") != null ? rs.getLong("id") : null);
        dto.setFirstName(rs.getString("first_name") != null ? rs.getString("first_name") : null);
        dto.setLastName(rs.getString("last_name") != null ? rs.getString("last_name") : null);
        dto.setEmail(rs.getString("email") != null ? rs.getString("email") : null);
        return dto;
    }    
}
