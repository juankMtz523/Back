package com.gtim.service_orders.service;

import java.util.List;
import com.gtim.service_orders.dto.CatProposalStatusDTO;

public interface CatProposalStatusService {
    List<CatProposalStatusDTO> getActiveProposalStatuses();
}
