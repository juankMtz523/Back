package com.gtim.service_orders.mapper;

import com.gtim.service_orders.dto.CatResourceDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CatResourceRowMapper implements RowMapper<CatResourceDTO> {

    @Override
    public CatResourceDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CatResourceDTO dto = new CatResourceDTO();
        dto.setId(rs.getString("id") != null ? rs.getLong("id") : null);
        dto.setFirstName(rs.getString("first_name") != null ? rs.getString("first_name") : null);
        dto.setLastName(rs.getString("last_name") != null ? rs.getString("last_name") : null);
        dto.setEmail(rs.getString("email") != null ? rs.getString("email") : null);
        dto.setPhone(rs.getString("phone") != null ? rs.getString("phone") : null);
        dto.setAreaId(rs.getString("area_id") != null ? rs.getLong("area_id") : null);
        dto.setAreaName((rs.getString("areaname") != null ? rs.getString("areaname") : null));
        dto.setRoleId((rs.getString("rol_id") != null ? rs.getLong("rol_id") : null));
        dto.setRoleName((rs.getString("rolename") != null ? rs.getString("rolename") : null));
        dto.setCoordinatorId((rs.getString("coordinator_id") != null ? rs.getLong("coordinator_id") : null));
        dto.setCoordinatorName((rs.getString("namecoordinator") != null ? rs.getString("namecoordinator") : null));
        dto.setCoordinatorEmail((rs.getString("emailcoordinator") != null ? rs.getString("emailcoordinator") : null));
        dto.setActive((rs.getString("active") != null ? rs.getBoolean("active") : null));
        dto.setPctAsignacion((rs.getString("pctasignacion") != null ? rs.getLong("pctasignacion") : null));
        
        return dto;
    }
}
