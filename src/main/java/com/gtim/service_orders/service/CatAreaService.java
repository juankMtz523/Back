package com.gtim.service_orders.service;

import com.gtim.service_orders.dto.CatAreaDTO;
import java.util.List;

public interface CatAreaService {

    List<CatAreaDTO> getActiveAreas(Long tipoArea);
}
