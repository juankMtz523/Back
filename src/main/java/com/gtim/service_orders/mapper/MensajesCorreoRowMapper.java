package com.gtim.service_orders.mapper;

import com.gtim.service_orders.dto.MensajesCorreoDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class MensajesCorreoRowMapper implements RowMapper<MensajesCorreoDTO> {
    @Override
    public MensajesCorreoDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
        MensajesCorreoDTO dto = new MensajesCorreoDTO();
        dto.setMensajeCoordinador(rs.getString("mensajeCoordinador") != null ? rs.getString("mensajeCoordinador") : null);
        dto.setMensajeColaborador(rs.getString("mensajeColaborador") != null ? rs.getString("mensajeColaborador") : null);
        dto.setNombreCoordinador(rs.getString("nombreCoordinador") != null ? rs.getString("nombreCoordinador") : null);
        dto.setCorreoCoordinador(rs.getString("correoCoordinador") != null ? rs.getString("correoCoordinador") : null);
        dto.setNombreColaborador(rs.getString("nombreColaborador") != null ? rs.getString("nombreColaborador") : null);
        dto.setCorreoColaborador(rs.getString("correoColaborador") != null ? rs.getString("correoColaborador") : null);
        dto.setTitulo(rs.getString("titulo") != null ? rs.getString("titulo") : null);
        
        return dto;
    }    
}
