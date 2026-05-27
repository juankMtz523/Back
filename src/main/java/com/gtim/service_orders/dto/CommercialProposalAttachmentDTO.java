package com.gtim.service_orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommercialProposalAttachmentDTO {

    private Long id;

    private Integer sectionTypeId;
    private String sectionTypeName;

    private String originalName;
    private String storedName;
    private String filePath;
    private String mimeType;
    private BigDecimal fileSizeMb;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime uploadedAt;
}


