package com.gtim.service_orders.service;

import java.util.List;

import com.gtim.service_orders.dto.CatRoleDTO;

public interface CatRoleService {
    List<CatRoleDTO> getActiveRoles();
}
