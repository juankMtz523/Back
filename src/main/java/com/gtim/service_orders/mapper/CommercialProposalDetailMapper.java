package com.gtim.service_orders.mapper;

import org.springframework.stereotype.Component;

import com.gtim.service_orders.dto.CommercialProposalAttachmentDTO;
import com.gtim.service_orders.dto.CommercialProposalDetailDTO;
import com.gtim.service_orders.dto.CommercialProposalRoleDTO;
import com.gtim.service_orders.dto.CommercialProposalSectionDTO;
import com.gtim.service_orders.entity.CommercialProposal;
import com.gtim.service_orders.entity.CommercialProposalAttachment;
import com.gtim.service_orders.entity.CommercialProposalRole;
import com.gtim.service_orders.entity.CommercialProposalSection;

@Component
public class CommercialProposalDetailMapper {

    public CommercialProposalDetailDTO toDto(CommercialProposal entity) {

        CommercialProposalDetailDTO dto = new CommercialProposalDetailDTO();

        dto.setId(entity.getId());
        dto.setProposalFolio(entity.getProposalFolio());

        // Service Request
        dto.setServiceRequestId(entity.getServiceRequest().getId());
        dto.setServiceRequestFolio(entity.getServiceRequest().getInternalFolio());

        // Status
        dto.setStatusId(entity.getStatus().getId());
        dto.setStatusName(entity.getStatus().getName());

        // Info
        dto.setDescription(entity.getDescription());
        dto.setAssumptions(entity.getAssumptions());
        dto.setInternalComments(entity.getInternalComments());

        dto.setTotalProjectCost(entity.getTotalProjectCost());
        dto.setSentToClientAt(entity.getSentToClientAt());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setActive(entity.getActive());

        // Roles
        dto.setRoles(
            entity.getRoles().stream()
                .map(this::toRoleDto)
                .toList()
        );

        // Sections
        dto.setSections(
            entity.getSections().stream()
                .map(this::toSectionDto)
                .toList()
        );

        // Attachments
        dto.setAttachments(
            entity.getAttachments().stream()
                .map(this::toAttachmentDto)
                .toList()
        );

        return dto;
    }

    private CommercialProposalRoleDTO toRoleDto(CommercialProposalRole r) {
        CommercialProposalRoleDTO dto = new CommercialProposalRoleDTO();
        dto.setId(r.getId());
        dto.setRoleId(r.getRole().getId());
        dto.setTypeDeveloper(r.getTypeDeveloper());
        dto.setName(r.getRole().getDescription());
        dto.setQuantity(r.getQuantity());
        dto.setMonths(r.getMonths());
        dto.setHourlyRate(r.getHourlyRate());
        dto.setAssignmentPercentage(r.getAssignmentPercentage());
        dto.setTotalRoleCost(r.getTotalRoleCost());
        dto.setAssignmentType(r.getAssignmentType());
        return dto;
    }

    private CommercialProposalSectionDTO toSectionDto(CommercialProposalSection s) {

        CommercialProposalSectionDTO dto = new CommercialProposalSectionDTO();

        dto.setId(s.getId());
        dto.setSectionType(s.getSectionType());
        dto.setContent(s.getContent());
        dto.setEnabled(s.getEnabled());

        return dto;
    }


    private CommercialProposalAttachmentDTO toAttachmentDto(CommercialProposalAttachment a) {

        CommercialProposalAttachmentDTO dto = new CommercialProposalAttachmentDTO();

        dto.setId(a.getId());

        dto.setSectionTypeId(a.getSectionType().ordinal());
        dto.setSectionTypeName(a.getSectionType().name());

        dto.setOriginalName(a.getOriginalName());
        dto.setStoredName(a.getStoredName());
        dto.setFilePath(a.getFilePath());
        dto.setMimeType(a.getMimeType());
        dto.setFileSizeMb(a.getFileSizeMb());
        dto.setUploadedAt(a.getCreatedAt());

        return dto;
    }

}
