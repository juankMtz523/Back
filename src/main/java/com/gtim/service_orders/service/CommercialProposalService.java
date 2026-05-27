package com.gtim.service_orders.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gtim.service_orders.dto.CommercialProposalCreateRequestDTO;
import com.gtim.service_orders.dto.CommercialProposalUpdateRequestDTO;
import com.gtim.service_orders.dto.CommercialProposalDetailDTO;
import com.gtim.service_orders.dto.CommercialProposalDuplicateDTO;
import com.gtim.service_orders.dto.CommercialProposalListDTO;
import com.gtim.service_orders.dto.CommercialProposalResponseDTO;
import com.gtim.service_orders.dto.CommercialProposalSectionDTO;
import com.gtim.service_orders.dto.CommercialProposalStatusChangeRequestDTO;
import com.gtim.service_orders.dto.CommercialProposalStatusChangeResponseDTO;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;

public interface CommercialProposalService {

    CommercialProposalResponseDTO create(
            CommercialProposalCreateRequestDTO request,
            String currentUser
    );

    Page<CommercialProposalListDTO> findByProjectId(Long projectId, Pageable pageable);

    CommercialProposalDetailDTO findDetailById(Long id);

    CommercialProposalStatusChangeResponseDTO changeStatus(Long proposalId,
            CommercialProposalStatusChangeRequestDTO request, String username);

    CommercialProposalDuplicateDTO duplicate(Long originalId, String username);

    void sendProposalToClient(Long proposalId, List<String> correos, String username);

    public ByteArrayResource downloadPDF(Long propuestaId);

    public CommercialProposalResponseDTO update(
            CommercialProposalUpdateRequestDTO request,
            String currentUser
    );

    public CommercialProposalSectionDTO addSection(
            Long id,
            CommercialProposalSectionDTO request,
            String currenUser
    );

    public CommercialProposalSectionDTO updateOrderService(
            Long id,
            CommercialProposalSectionDTO request,
            String currentUser
    );
}
