package com.gtim.service_orders.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gtim.service_orders.entity.CommercialProposal;
import com.gtim.service_orders.entity.CommercialProposalAttachment;
import com.gtim.service_orders.entity.TrxServiceOrder;
import com.gtim.service_orders.constants.AzureStorageParam;
import com.gtim.service_orders.enums.SectionType;
import com.gtim.service_orders.exception.BusinessException;
import com.gtim.service_orders.exception.ResourceNotFoundException;
import com.gtim.service_orders.repository.CommercialProposalAttachmentRepository;
import com.gtim.service_orders.repository.CommercialProposalRepository;
import com.gtim.service_orders.repository.ServiceOrderRepository;
import com.gtim.service_orders.service.CommercialProposalAttachmentService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommercialProposalAttachmentServiceImpl
        implements CommercialProposalAttachmentService {

    private final CommercialProposalRepository proposalRepository;
    private final CommercialProposalAttachmentRepository attachmentRepository;
    private final ServiceOrderRepository osRepo;
    private final AzureStorageParam asParam;

    @Value("${storage.commercial-proposals-path}")
    private String basePath;

    @Override
    public void upload(
            Long proposalId,
            SectionType sectionType,
            MultipartFile file,
            String user
    ) throws IOException {

        CommercialProposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException("La propuesta no existe"));

        // 1. Validar estatus (no editable)
        String statusName = proposal.getStatus().getName();

        if ("Aceptado por el cliente".equalsIgnoreCase(statusName)
                || "Rechazado".equalsIgnoreCase(statusName)) {
            throw new BusinessException(
                    "La propuesta no permite modificaciones en este estatus"
            );
        }

        // 2. Validar que la sección no tenga ya adjunto
        if (attachmentRepository.existsByProposalIdAndSectionType(proposalId, sectionType)) {
            throw new BusinessException(
                    "La sección " + sectionType.name() + " ya tiene un adjunto"
            );
        }

        // 3. Adjuntos NO obligatorios
        if (file == null || file.isEmpty()) {
            return; // se permite continuar sin adjunto
        }

        // 4. Validar tipo de archivo (solo imágenes)
        if (!List.of("image/jpeg", "image/png").contains(file.getContentType())) {
            throw new BusinessException(
                    "Solo se permiten imágenes en formato JPEG o PNG"
            );
        }

        // 5. Validar tamaño (5 MB maximo)
        long size = file.getSize();
        if (size > 5L * 1024 * 1024) {
            throw new BusinessException(
                    "El tamaño de la imagen debe ser máximo 5 MB"
            );
        }

        // 6. Construir nombre del archivo
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());

        String storedName = proposal.getProposalFolio()
                + "_" + sectionType.name() + "_01." + ext;

        // 7. Crear directorio y guardar archivo
        Path dir = Paths.get(basePath, proposalId.toString());
        Files.createDirectories(dir);

        Path filePath = dir.resolve(storedName);
        Files.copy(file.getInputStream(), filePath);

        // 8. Persistir adjunto
        attachmentRepository.save(
                CommercialProposalAttachment.builder()
                        .proposal(proposal)
                        .sectionType(sectionType)
                        .originalName(file.getOriginalFilename())
                        .storedName(storedName)
                        .filePath(filePath.toString())
                        .mimeType(file.getContentType())
                        .fileSizeMb(
                                BigDecimal.valueOf(size)
                                        .divide(BigDecimal.valueOf(1024 * 1024))
                        )
                        .createdBy(user)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public ByteArrayOutputStream downloadStorage(String fileName) {
        CloudStorageAccount storageAccount;
        CloudBlobClient blobClient = null;
        CloudBlobContainer container = null;
        ByteArrayOutputStream outputStream = null;
        try {
            storageAccount = CloudStorageAccount.parse(asParam.getStorageConnectionString());
            blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(asParam.getContainer());
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);
            outputStream = new ByteArrayOutputStream();
            blob.download(outputStream);
        } catch (URISyntaxException | InvalidKeyException | StorageException ex) {
            outputStream = null;
        }
        return outputStream;
    }

    @Override
    public boolean deleteStorage(String fileName) {
        CloudStorageAccount storageAccount;
        CloudBlobClient blobClient = null;
        CloudBlobContainer container = null;
        ByteArrayOutputStream outputStream = null;
        boolean eliminado = true;
        try {
            storageAccount = CloudStorageAccount.parse(asParam.getStorageConnectionString());
            blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(asParam.getContainer());
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);
            blob.delete();
        } catch (URISyntaxException | InvalidKeyException | StorageException ex) {
            eliminado = false;
        }
        return eliminado;
    }

    @Override
    public void duplicateStorage(Long proposalIdOri,
            CommercialProposal proposalNew,
            String user) {

        CloudStorageAccount storageAccount;
        CloudBlobClient blobClient = null;
        CloudBlobContainer container = null;
        ByteArrayOutputStream outputStream = null;

        try {
            List<CommercialProposalAttachment> originalAttach = attachmentRepository.findByProposalId(proposalIdOri);
            storageAccount = CloudStorageAccount.parse(asParam.getStorageConnectionString());
            blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(asParam.getContainer());

            CloudBlockBlob blobDownload;
            CloudBlockBlob blobUpload;
            String ext = "";
            String storedName = "";

            for (CommercialProposalAttachment cpa : originalAttach) {
                blobDownload = container.getBlockBlobReference(cpa.getStoredName());
                outputStream = new ByteArrayOutputStream();
                blobDownload.download(outputStream);
                ext = FilenameUtils.getExtension(cpa.getOriginalName());
                storedName = proposalNew.getProposalFolio() + "_" + cpa.getSectionType().name() + "_01." + ext;
                blobUpload = container.getBlockBlobReference(storedName);
                blobUpload.upload(new ByteArrayInputStream(outputStream.toByteArray()), outputStream.size());

                // 8. Persistir adjunto
                attachmentRepository.save(
                        CommercialProposalAttachment.builder()
                                .proposal(proposalNew)
                                .sectionType(cpa.getSectionType())
                                .originalName(cpa.getOriginalName())
                                .storedName(storedName)
                                .filePath("https://capexdevjc.blob.core.windows.net/somgtim/" + storedName)
                                .mimeType(cpa.getMimeType())
                                .fileSizeMb(cpa.getFileSizeMb())
                                .createdBy(user)
                                .createdAt(LocalDateTime.now())
                                .build()
                );
            }
        } catch (URISyntaxException | InvalidKeyException | StorageException ex) {
            throw new BusinessException(ex.getMessage());
        } catch (IOException ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public void uploadOS(
            Long proposalId,
            Long idOrderService,
            String comments,
            MultipartFile file,
            String user
    ) throws IOException, URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount storageAccount;
        CloudBlobClient blobClient = null;
        CloudBlobContainer container = null;

        CommercialProposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException("La propuesta no existe"));

        if (osRepo.existsByProposalId(proposalId)) {
            TrxServiceOrder serviceOrderList = osRepo.findByProposalId(proposalId);
            deleteStorage(serviceOrderList.getFileName());
        }

        // 3. Adjuntos NO obligatorios
        if (file == null || file.isEmpty()) {
            return; // se permite continuar sin adjunto
        }

        // 4. Validar tipo de archivo (solo PDF)
        if (!List.of("application/pdf").contains(file.getContentType())) {
            throw new BusinessException(
                    "Solo se permiten archivos PDF para la Orden de Servicio"
            );
        }

        // 5. Validar tamaño (20 MB maximo)
        Long size = file.getSize();
        if (size > 20L * 1024 * 1024) {
            throw new BusinessException(
                    "El tamaño del PDF debe ser máximo 20 MB"
            );
        }

        // 6. Construir nombre del archivo
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());

        String storedName = "OS_" + proposal.getProposalFolio() + "_01." + ext;

        // 7. Crear directorio y guardar archivo
        storageAccount = CloudStorageAccount.parse(asParam.getStorageConnectionString());
        blobClient = storageAccount.createCloudBlobClient();
        container = blobClient.getContainerReference(asParam.getContainer());

        CloudBlockBlob blob = container.getBlockBlobReference(storedName);
        blob.upload(file.getInputStream(), file.getSize());

        // 8. Persistir adjunto
        
        Long idOS = idOrderService != 0L ? idOrderService : null;
        
        osRepo.save(
                TrxServiceOrder.builder()
                        .idServiceOrder(idOS)
                        .proposal(proposal)
                        .osFolio("OS_" + proposal.getProposalFolio())
                        .fileName(storedName)
                        .filePath("https://capexdevjc.blob.core.windows.net/somgtim/" + storedName)
                        .comments(comments)
                        .createdBy(user)
                        .createdAt(LocalDateTime.now())
                        .active(true)
                        .build()
        );

    }

    @Override
    public void uploadStorage(
            Long proposalId,
            SectionType sectionType,
            MultipartFile file,
            String user
    ) throws IOException, URISyntaxException, InvalidKeyException, StorageException {
        CloudStorageAccount storageAccount;
        CloudBlobClient blobClient = null;
        CloudBlobContainer container = null;

        CommercialProposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException("La propuesta no existe"));

        // 1. Validar estatus (no editable)
        String statusName = proposal.getStatus().getName();

        if ("Aceptado por el cliente".equalsIgnoreCase(statusName) || "Rechazado".equalsIgnoreCase(statusName)) {
            throw new BusinessException(
                    "La propuesta no permite modificaciones en este estatus"
            );
        }

        // 2. Validar que la sección no tenga ya adjunto y se elimina para actualizar por el nuevo archivo
        if (attachmentRepository.existsByProposalIdAndSectionType(proposalId, sectionType)) {
            CommercialProposalAttachment cpa = attachmentRepository.findByProposalIdAndSectionType(proposalId, sectionType);
            deleteStorage(cpa.getStoredName());
            attachmentRepository.delete(cpa);
        }

        // 3. Adjuntos NO obligatorios
        if (file == null || file.isEmpty()) {
            return; // se permite continuar sin adjunto
        }

        // 4. Validar tipo de archivo (solo imágenes)
        if (!List.of("image/jpeg", "image/png").contains(file.getContentType())) {
            throw new BusinessException(
                    "Solo se permiten imágenes en formato JPEG o PNG"
            );
        }

        // 5. Validar tamaño (5 MB maximo)
        long size = file.getSize();
        if (size > 5L * 1024 * 1024) {
            throw new BusinessException(
                    "El tamaño de la imagen debe ser máximo 5 MB"
            );
        }

        // 6. Construir nombre del archivo
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());

        String storedName = proposal.getProposalFolio()
                + "_" + sectionType.name() + "_01." + ext;

        // 7. Crear directorio y guardar archivo
        storageAccount = CloudStorageAccount.parse(asParam.getStorageConnectionString());
        blobClient = storageAccount.createCloudBlobClient();
        container = blobClient.getContainerReference(asParam.getContainer());

        CloudBlockBlob blob = container.getBlockBlobReference(storedName);
        blob.upload(file.getInputStream(), file.getSize());

        // 8. Persistir adjunto
        attachmentRepository.save(
                CommercialProposalAttachment.builder()
                        .proposal(proposal)
                        .sectionType(sectionType)
                        .originalName(file.getOriginalFilename())
                        .storedName(storedName)
                        .filePath("https://capexdevjc.blob.core.windows.net/somgtim/" + storedName)
                        .mimeType(file.getContentType())
                        .fileSizeMb(BigDecimal.valueOf(size).divide(BigDecimal.valueOf(1024 * 1024)))
                        .createdBy(user)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

    }

}
