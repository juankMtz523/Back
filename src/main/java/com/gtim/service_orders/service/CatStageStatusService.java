package com.gtim.service_orders.service;

import java.util.List;
import com.gtim.service_orders.dto.CatStageStatusDTO;

public interface CatStageStatusService {

    List<CatStageStatusDTO> getActiveStageStatus();
}
