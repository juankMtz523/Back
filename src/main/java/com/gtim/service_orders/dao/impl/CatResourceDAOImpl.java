package com.gtim.service_orders.dao.impl;

import com.gtim.service_orders.dao.CatResourceDAO;
import com.gtim.service_orders.dto.CatResourceDTO;
import com.gtim.service_orders.dto.ProyectosColaboradorDTO;
import com.gtim.service_orders.dto.ColaboradoresCoordinadorDTO;
import com.gtim.service_orders.mapper.CatResourceRowMapper;
import com.gtim.service_orders.mapper.ProyectosColaboradorRowMapper;
import com.gtim.service_orders.mapper.ColaboradoresCoordinadorRowMapper;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CatResourceDAOImpl implements CatResourceDAO {

    private final JdbcTemplate jdbcTemplate;

    public CatResourceDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProyectosColaboradorDTO> getProyectosXColaborador(Long roleId, Long resourceId) {
        jdbcTemplate.execute("BEGIN");
        String cursorName = jdbcTemplate.queryForObject("SELECT get_proyectoxcolaborador_cursor(?,?)", String.class, roleId, resourceId);
        List<ProyectosColaboradorDTO> projectsResource = jdbcTemplate.query("FETCH ALL IN \"" + cursorName + "\"", new ProyectosColaboradorRowMapper());
        jdbcTemplate.execute("CLOSE \"" + cursorName + "\"");
        jdbcTemplate.execute("COMMIT");
        return projectsResource;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ColaboradoresCoordinadorDTO> getColaboradoresXCoordinador(Long coordinadorId) {
        jdbcTemplate.execute("BEGIN");
        String cursorName = jdbcTemplate.queryForObject("SELECT get_resourcesxcoordinator_cursor(?)", String.class, coordinadorId);
        List<ColaboradoresCoordinadorDTO> resourceCoordinator = jdbcTemplate.query("FETCH ALL IN \"" + cursorName + "\"", new ColaboradoresCoordinadorRowMapper());
        jdbcTemplate.execute("CLOSE \"" + cursorName + "\"");
        jdbcTemplate.execute("COMMIT");
        return resourceCoordinator;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CatResourceDTO> getResourceXProposal(Long proposalId) {
        jdbcTemplate.execute("BEGIN");
        String cursorName = jdbcTemplate.queryForObject("SELECT get_resourcesxproposal_cursor(?)", String.class, proposalId);
        List<CatResourceDTO> resourceProposal = jdbcTemplate.query("FETCH ALL IN \"" + cursorName + "\"", new CatResourceRowMapper());
        jdbcTemplate.execute("CLOSE \"" + cursorName + "\"");
        jdbcTemplate.execute("COMMIT");
        return resourceProposal;
    }    
    
    @Override
    public List<String> desactivarCoordinador(Long resourceIdAnt, Long roleId, Long resourceIdNew, String user) {

        List<String> cambios = new ArrayList<>();
        String sqlResource = "update cat_resource set coordinator_id = ?, updated_by = ?, updated_at = TIMEZONE('America/Mexico_City',now()) where coordinator_id = ?";
        String sqlAssign = "update trx_resource_assignment set resource_id = ?, updated_by = ?, updated_at = TIMEZONE('America/Mexico_City',now()) where resource_id = ? and role_id = ?";
        String sqlInactive = "update cat_coordinator set active = false, updated_by = ?, updated_at = TIMEZONE('America/Mexico_City',now())  where id = ?";

        int rowsAffected = 0;

        rowsAffected = jdbcTemplate.update(sqlResource, resourceIdNew, user, resourceIdAnt);
        cambios.add("Colaboradores actualizados: " + rowsAffected);

        rowsAffected = jdbcTemplate.update(sqlAssign, resourceIdNew, user, resourceIdAnt, roleId);
        cambios.add("Asignaciones actualizados: " + rowsAffected);

        rowsAffected = jdbcTemplate.update(sqlInactive, user, resourceIdAnt);
        cambios.add("Coordinadores actualizados: " + rowsAffected);

        return cambios;
    }
    
    @Override
    public List<String> desactivarColaborador(Long resourceIdAnt, String user) {

        List<String> cambios = new ArrayList<>();
        String sqlResource = "update cat_resource set active = false, updated_by = ?, updated_at = TIMEZONE('America/Mexico_City',now()) where  id = ?";

        int rowsAffected = 0;

        rowsAffected = jdbcTemplate.update(sqlResource, user, resourceIdAnt);
        cambios.add("Colaboradores actualizados: " + rowsAffected);

        return cambios;
    }    
}
