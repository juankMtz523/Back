package com.gtim.service_orders.dao.impl;

import com.gtim.service_orders.dao.ResourceAssignmentDAO;
import com.gtim.service_orders.dto.DashboardAssignmentDTO;
import com.gtim.service_orders.dto.ProyectoPropuestasDTO;
import com.gtim.service_orders.dto.RolesPropuestaDTO;
import com.gtim.service_orders.mapper.AssignResourcesRowMapper;
import com.gtim.service_orders.mapper.DashboardAssignRowMapper;
import com.gtim.service_orders.mapper.ProjectProposalRowMapper;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ResourceAssignmentDAOImpl implements ResourceAssignmentDAO {

    private final JdbcTemplate jdbcTemplate;

    public ResourceAssignmentDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DashboardAssignmentDTO> getDashboard(Long estatus, String username) {
        jdbcTemplate.execute("BEGIN");
        String cursorName = jdbcTemplate.queryForObject("SELECT get_asignaciones_cursor_v2(?,?)", String.class, estatus, username);
        List<DashboardAssignmentDTO> dashAssign = jdbcTemplate.query("FETCH ALL IN \"" + cursorName + "\"", new DashboardAssignRowMapper());
        jdbcTemplate.execute("CLOSE \"" + cursorName + "\"");
        jdbcTemplate.execute("COMMIT");
        return dashAssign;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProyectoPropuestasDTO> getProjectProposals(Long idProject) {
        jdbcTemplate.execute("BEGIN");
        String cursorName = jdbcTemplate.queryForObject("SELECT get_proyectopropuestas_cursor(?)", String.class, idProject);
        List<ProyectoPropuestasDTO> listProposal = jdbcTemplate.query("FETCH ALL IN \"" + cursorName + "\"", new ProjectProposalRowMapper());
        jdbcTemplate.execute("CLOSE \"" + cursorName + "\"");
        jdbcTemplate.execute("COMMIT");
        return listProposal;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RolesPropuestaDTO> getRolesProposal(Long idProposal) {
        jdbcTemplate.execute("BEGIN");
        String cursorName = jdbcTemplate.queryForObject("SELECT get_asignacion_roles_cursor(?)", String.class, idProposal);
        List<RolesPropuestaDTO> listRoles = jdbcTemplate.query("FETCH ALL IN \"" + cursorName + "\"", new AssignResourcesRowMapper());
        jdbcTemplate.execute("CLOSE \"" + cursorName + "\"");
        jdbcTemplate.execute("COMMIT");
        return listRoles;
    }

}
