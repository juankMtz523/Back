package com.gtim.service_orders.service;

import java.util.List;

import com.gtim.service_orders.dto.CatClientDTO;

public interface CatClientService {

    List<CatClientDTO> getActiveClients();
}
