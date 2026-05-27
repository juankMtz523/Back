package com.gtim.service_orders.dao.impl;

import com.gtim.service_orders.dao.MensajesCorreoDAO;
import com.gtim.service_orders.dto.MensajesCorreoDTO;
import com.gtim.service_orders.dto.CorreoCoordinadorDTO;
import com.gtim.service_orders.mapper.MensajesCorreoRowMapper;
import com.gtim.service_orders.mapper.CorreoCoordinadorRowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MensajesCorreoDAOImpl implements MensajesCorreoDAO {

    private final JdbcTemplate jdbcTemplate;

    public MensajesCorreoDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<MensajesCorreoDTO> getMensajesCorreo() {
        jdbcTemplate.execute("BEGIN");
        String cursorName = jdbcTemplate.queryForObject("SELECT get_mensajes_correo_cursor()", String.class);
        List<MensajesCorreoDTO> mensajes = jdbcTemplate.query("FETCH ALL IN \"" + cursorName + "\"", new MensajesCorreoRowMapper());
        jdbcTemplate.execute("CLOSE \"" + cursorName + "\"");
        jdbcTemplate.execute("COMMIT");
        return mensajes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CorreoCoordinadorDTO> getCoordinadoresProyectoNuevo(Long proposalId) {
        String sql = "select distinct cc.\"name\" nombre, cc.email correo from trx_commercial_proposal_role tcpr inner join cat_role cr on tcpr.role_id = cr.id inner join cat_coordinator cc on cr.area_id = cc.engineering_id where tcpr.proposal_id = ? and cc.active = true";
        List<CorreoCoordinadorDTO> correos = jdbcTemplate.query(
                sql,
                new CorreoCoordinadorRowMapper(), proposalId
        );

        return correos;
    }
    
    @Override
    public void setRechazadosPropuestasX30Dias(){
        String sqlIdsPropuesta = "select tcp.id from trx_commercial_proposal tcp where tcp.status_id = 6 and tcp.created_at::date = (tcp.created_at::date + (interval '30 days'))::date";
        String updateStatus = "update trx_commercial_proposal set status_id = 5, comments = 'Propuesta Rechazada despues de 30 días sin respuesta' where id = ?";
        
        List<Long> ids = jdbcTemplate.queryForList(sqlIdsPropuesta, Long.class);
        
        for(Long i : ids){
            jdbcTemplate.update(updateStatus, i);
        }
    }
}
