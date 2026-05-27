package com.gtim.service_orders.service;

import com.gtim.service_orders.dto.CatProjectStatusDTO;

import java.util.List;

public interface CatProjectStatusService {

    List<CatProjectStatusDTO> getActiveStatuses();
}
