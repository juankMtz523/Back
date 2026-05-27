package com.gtim.service_orders.service;

import com.gtim.service_orders.entity.CommercialProposal;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.gtim.service_orders.enums.SectionType;
import com.microsoft.azure.storage.StorageException;
import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public interface CommercialProposalAttachmentService {

    void upload(
            Long proposalId,
            SectionType sectionType,
            MultipartFile file,
            String user
    ) throws IOException;

    public void uploadOS(
            Long proposalId,
            Long idOrderService,
            String comments,
            MultipartFile file,
            String user
    ) throws IOException, URISyntaxException, InvalidKeyException, StorageException;

    public void uploadStorage(
            Long proposalId,
            SectionType sectionType,
            MultipartFile file,
            String user
    ) throws IOException, URISyntaxException, InvalidKeyException, StorageException;

    ByteArrayOutputStream downloadStorage(String fileName);

    boolean deleteStorage(String fileName);

    void duplicateStorage(Long proposalIdOri,
            CommercialProposal proposalNew,
            String user);
}
