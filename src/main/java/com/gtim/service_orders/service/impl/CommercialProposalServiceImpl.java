package com.gtim.service_orders.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gtim.service_orders.dto.CommercialProposalAttachmentDTO;
import com.gtim.service_orders.dto.CommercialProposalCreateRequestDTO;
import com.gtim.service_orders.dto.CommercialProposalDetailDTO;
import com.gtim.service_orders.dto.CommercialProposalDuplicateDTO;
import com.gtim.service_orders.dto.CommercialProposalListDTO;
import com.gtim.service_orders.dto.CommercialProposalResponseDTO;
import com.gtim.service_orders.dto.CommercialProposalRoleDTO;
import com.gtim.service_orders.dto.CommercialProposalRoleRequestDTO;
import com.gtim.service_orders.dto.CommercialProposalSectionDTO;
import com.gtim.service_orders.dto.CommercialProposalStatusChangeRequestDTO;
import com.gtim.service_orders.dto.CommercialProposalStatusChangeResponseDTO;
import com.gtim.service_orders.dto.CommercialProposalUpdateRequestDTO;
import com.gtim.service_orders.dto.CatHoraAsignacionDTO;
import com.gtim.service_orders.entity.CatProjectStatus;
import com.gtim.service_orders.entity.CatProposalStatus;
import com.gtim.service_orders.entity.CatRole;
import com.gtim.service_orders.entity.CommercialProposal;
import com.gtim.service_orders.entity.CommercialProposalAttachment;
import com.gtim.service_orders.entity.CommercialProposalRole;
import com.gtim.service_orders.entity.CommercialProposalSection;
import com.gtim.service_orders.entity.ServiceRequest;
import com.gtim.service_orders.entity.TrxServiceOrder;
import com.gtim.service_orders.enums.ProjectStatusEnum;
import com.gtim.service_orders.enums.ProposalStatusEnum;
import com.gtim.service_orders.enums.SectionType;
import com.gtim.service_orders.exception.BusinessException;
import com.gtim.service_orders.exception.ResourceNotFoundException;
import com.gtim.service_orders.mapper.CommercialProposalDetailMapper;
import com.gtim.service_orders.notification.EmailService;
import com.gtim.service_orders.notification.PDFService;
import com.gtim.service_orders.repository.CatProjectStatusRepository;
import com.gtim.service_orders.repository.CatProposalStatusRepository;
import com.gtim.service_orders.repository.CatRoleRepository;
import com.gtim.service_orders.repository.CommercialProposalAttachmentRepository;
import com.gtim.service_orders.repository.CommercialProposalRepository;
import com.gtim.service_orders.repository.ServiceRequestRepository;
import com.gtim.service_orders.repository.CommercialProposalRoleRepository;
import com.gtim.service_orders.repository.CommercialProposalSectionRepository;
import com.gtim.service_orders.repository.ServiceOrderRepository;
import com.gtim.service_orders.service.CommercialProposalService;
import com.gtim.service_orders.service.CatHoraAsginacionService;
import com.gtim.service_orders.service.CommercialProposalAttachmentService;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommercialProposalServiceImpl implements CommercialProposalService {

    private final CommercialProposalRepository proposalRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final CatProposalStatusRepository catProposalStatusRepository;
    private final CommercialProposalDetailMapper commercialProposalDetailMapper;
    private final CatRoleRepository catRoleRepository;
    private final CommercialProposalAttachmentRepository attachmentRepository;
    private final CommercialProposalAttachmentService attachmentService;
    private final CommercialProposalRoleRepository rolesRepository;
    private final CommercialProposalSectionRepository sectionRepository;
    private final CatProjectStatusRepository catProjectStatusRepository;
    private final ServiceOrderRepository osRepo;
    private final EmailService emailService;
    private final CatHoraAsginacionService horaService;
    private final PDFService pdfService;
    private final ApplicationEventPublisher eventPublisher;

    // CREATE AND DUPLICATE
    @Override
    public CommercialProposalResponseDTO create(
            CommercialProposalCreateRequestDTO request,
            String user
    ) {

        List<CatHoraAsignacionDTO> horas = horaService.getHorasAsignacion();

        ServiceRequest project = serviceRequestRepository.findById(request.getServiceRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no existe"));

        String folio = generateUniqueFolio(project);

        CatProposalStatus initialStatus = catProposalStatusRepository
                .findById(request.getStatus())
                .orElseThrow(() -> new IllegalArgumentException("Status no encontrado"));

        CommercialProposal proposal = CommercialProposal.builder()
                .serviceRequest(project)
                .proposalFolio(folio)
                .description(request.getDescription())
                .assumptions(request.getAssumptions())
                .internalComments(request.getInternalComments())
                .status(initialStatus)
                .active(true)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();

        proposal.setRoles(request.getRoles().stream()
                .map(r -> buildRoleEntity(r, proposal, horas, user))
                .toList());

        proposal.setSections(request.getSections().stream()
                .map(s -> CommercialProposalSection.builder()
                .proposal(proposal)
                .sectionType(s.getSectionType())
                .content(s.getContent())
                .enabled(true)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build())
                .toList());

        /*if (request.getAttachments() != null) {
            proposal.setAttachments(request.getAttachments().stream()
                    .map(a -> duplicateAttachment(a.getId(), proposal, user))
                    .toList());
        }*/
        proposal.setTotalProjectCost(
                proposal.getRoles().stream()
                        .map(CommercialProposalRole::getTotalRoleCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        proposal.setComments(null);

        CommercialProposal saved = proposalRepository.save(proposal);

        return CommercialProposalResponseDTO.builder()
                .id(saved.getId())
                .proposalFolio(saved.getProposalFolio())
                .serviceRequestId(project.getId())
                .statusId(saved.getStatus().getId())
                .totalProjectCost(saved.getTotalProjectCost())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // DUPLICATE PREVIEW (GET)
    @Override
    public CommercialProposalDuplicateDTO duplicate(Long originalId, String username) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<CatHoraAsignacionDTO> horas = horaService.getHorasAsignacion();
        List<CommercialProposalRoleRequestDTO> listNewRole = new ArrayList<>();
        List<CommercialProposalSectionDTO> listNewSection = new ArrayList<>();
        CommercialProposalRoleRequestDTO newRole;
        CommercialProposalSectionDTO newSection;

        CommercialProposal original = proposalRepository.findById(originalId)
                .orElseThrow(() -> new ResourceNotFoundException("CommercialProposal", "id", originalId));

        List<CommercialProposalRole> listOriginalRole = rolesRepository.findByProposal(original, sort);
        List<CommercialProposalSection> listOriginalSection = sectionRepository.findByProposal(original, sort);

        ServiceRequest project = original.getServiceRequest();

        String newFolio = generatePreviewFolio(project);

        CatProposalStatus initialStatus = catProposalStatusRepository
                .findById(6L)
                .orElseThrow(() -> new IllegalArgumentException("Status no encontrado"));

        for (CommercialProposalRole r : original.getRoles()) {
            newRole = new CommercialProposalRoleRequestDTO();
            newRole.setRoleId(r.getRole().getId());
            newRole.setTypeDeveloper(r.getTypeDeveloper());
            newRole.setQuantity(r.getQuantity());
            newRole.setHourlyRate(r.getHourlyRate());
            newRole.setAssignmentPercentage(r.getAssignmentPercentage());
            newRole.setAssignmentType(r.getAssignmentType());
            newRole.setMonths(r.getMonths());

            listNewRole.add(newRole);
        }

        CommercialProposal nuevo = CommercialProposal.builder()
                .serviceRequest(project)
                .proposalFolio(newFolio)
                .description(original.getDescription())
                .assumptions(original.getAssumptions())
                .internalComments(original.getInternalComments())
                .status(initialStatus)
                .active(true)
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build();

        nuevo.setRoles(listNewRole.stream()
                .map(r -> buildRoleEntity(r, nuevo, horas, username))
                .toList());

        nuevo.setSections(original.getSections().stream()
                .map(s -> CommercialProposalSection.builder()
                .proposal(nuevo)
                .sectionType(s.getSectionType())
                .content(s.getContent())
                .enabled(true)
                .createdBy(username)
                .createdAt(LocalDateTime.now())
                .build())
                .toList());

        nuevo.setTotalProjectCost(
                nuevo.getRoles().stream()
                        .map(CommercialProposalRole::getTotalRoleCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        nuevo.setComments(null);

        CommercialProposal proposal = proposalRepository.save(nuevo);

        attachmentService.duplicateStorage(originalId, proposal, username);

        return CommercialProposalDuplicateDTO.builder()
                .originalProposalId(nuevo.getId())
                .originalFolio(original.getProposalFolio())
                .serviceRequestId(project.getId())
                .serviceRequestFolio(project.getInternalFolio())
                .previewFolio(newFolio)
                .description(nuevo.getDescription())
                .assumptions(nuevo.getAssumptions())
                .internalComments(nuevo.getInternalComments())
                .roles(mapRoles(nuevo))
                .sections(mapSections(nuevo))
                .attachments(mapAttachments(nuevo))
                .comments(nuevo.getComments())
                .build();
    }

    private String generateUniqueFolio(ServiceRequest project) {
        String folio;
        do {
            long next = proposalRepository.countByServiceRequest(project) + 1;
            folio = project.getInternalFolio() + "_PC_" + String.format("%03d", next);
        } while (proposalRepository.existsByProposalFolio(folio));
        return folio;
    }

    private String generatePreviewFolio(ServiceRequest project) {
        long next = proposalRepository.countByServiceRequest(project) + 1;
        return project.getInternalFolio() + "_PC_" + String.format("%03d", next);
    }

    private CommercialProposalRole buildRoleEntity(
            CommercialProposalRoleRequestDTO r,
            CommercialProposal proposal,
            List<CatHoraAsignacionDTO> horas,
            String user
    ) {
        BigDecimal totalHoras = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);

        CatRole role = catRoleRepository.findById(r.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no existe"));

        CatHoraAsignacionDTO hora = horas.stream().filter(h -> h.getTipoAsignacion().equals(r.getAssignmentType()) && h.getPorcentaje() == r.getAssignmentPercentage().longValue()).findAny().orElse(null);

        if (hora != null) {
            totalHoras = hora.getValor();
            total = totalHoras
                    .multiply(r.getMonths())
                    .multiply(BigDecimal.valueOf(r.getQuantity()))
                    .multiply(r.getHourlyRate());
        } else {
            totalHoras = BigDecimal.valueOf(1);
            total = totalHoras
                    .multiply(r.getMonths())
                    .multiply(BigDecimal.valueOf(r.getQuantity()))
                    .multiply(r.getHourlyRate())
                    .multiply(r.getAssignmentPercentage().divide(BigDecimal.valueOf(100)));
        }

        return CommercialProposalRole.builder()
                .proposal(proposal)
                .role(role)
                .typeDeveloper(r.getTypeDeveloper())
                .quantity(r.getQuantity())
                .hourlyRate(r.getHourlyRate())
                .assignmentPercentage(r.getAssignmentPercentage())
                .assignmentType(r.getAssignmentType())
                .months(r.getMonths())
                .totalRoleCost(total)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private CommercialProposalAttachment duplicateAttachment(
            Long attachmentId,
            CommercialProposal proposal,
            String user
    ) {

        CommercialProposalAttachment original = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("Adjunto no existe"));

        String newStoredName = generateNewStoredName(original.getStoredName());

        Path source = Paths.get(original.getFilePath());
        Path target = source.getParent().resolve(newStoredName);

        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al duplicar adjunto", e);
        }

        return CommercialProposalAttachment.builder()
                .proposal(proposal)
                .sectionType(original.getSectionType())
                .originalName(original.getOriginalName())
                .storedName(newStoredName)
                .filePath(target.toString())
                .mimeType(original.getMimeType())
                .fileSizeMb(original.getFileSizeMb())
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String generateNewStoredName(String current) {
        Pattern p = Pattern.compile("_(\\d{2})(\\..+)$");
        Matcher m = p.matcher(current);

        if (m.find()) {
            int n = Integer.parseInt(m.group(1)) + 1;
            return current.replaceFirst("_(\\d{2})(\\..+)$", "_" + String.format("%02d", n) + m.group(2));
        }
        int dot = current.lastIndexOf(".");
        return current.substring(0, dot) + "_02" + current.substring(dot);
    }

    private List<CommercialProposalRoleRequestDTO> mapRoles(CommercialProposal p) {
        return p.getRoles().stream().map(r -> {
            CommercialProposalRoleRequestDTO dto = new CommercialProposalRoleRequestDTO();
            dto.setRoleId(r.getRole().getId());
            dto.setQuantity(r.getQuantity());
            dto.setMonths(r.getMonths());
            dto.setHourlyRate(r.getHourlyRate());
            dto.setAssignmentPercentage(r.getAssignmentPercentage());
            dto.setAssignmentType(r.getAssignmentType());
            return dto;
        }).toList();
    }

    private List<CommercialProposalSectionDTO> mapSections(CommercialProposal p) {
        return p.getSections().stream().map(s -> {
            CommercialProposalSectionDTO dto = new CommercialProposalSectionDTO();
            dto.setSectionType(s.getSectionType());
            dto.setContent(s.getContent());
            dto.setEnabled(s.getEnabled());
            return dto;
        }).toList();
    }

    private List<CommercialProposalAttachmentDTO> mapAttachments(CommercialProposal p) {
        if (p.getAttachments() != null) {
            return p.getAttachments().stream().map(a -> {
                CommercialProposalAttachmentDTO dto = new CommercialProposalAttachmentDTO();
                dto.setId(a.getId());
                dto.setOriginalName(a.getOriginalName());
                dto.setMimeType(a.getMimeType());
                dto.setFileSizeMb(a.getFileSizeMb());
                dto.setUploadedAt(a.getCreatedAt());
                return dto;
            }).toList();
        } else {
            return null;
        }
    }

    @Override
    public Page<CommercialProposalListDTO> findByProjectId(Long projectId, Pageable pageable) {
        ServiceRequest sr = serviceRequestRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Service Request no existe"));

        return proposalRepository.findByServiceRequestAndActiveTrue(sr, pageable)
                .map(p -> {
                    CommercialProposalListDTO dto = new CommercialProposalListDTO();
                    dto.setId(p.getId());
                    dto.setProposalFolio(p.getProposalFolio());
                    dto.setStatusId(p.getStatus().getId());
                    dto.setStatusName(p.getStatus().getName());
                    dto.setComments(p.getComments());
                    dto.setExisteOrdenServicio(this.validarExisteOS(p.getId()));
                    return dto;
                });
    }

    @Override
    public CommercialProposalDetailDTO findDetailById(Long id) {
        CommercialProposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommercialProposal", "id", id));

        CommercialProposalDetailDTO proposalDTO = commercialProposalDetailMapper.toDto(proposal);

        List<CommercialProposalSectionDTO> sectionList = new ArrayList<>();
        List<CommercialProposalAttachmentDTO> attachmentList = new ArrayList<>();

        CommercialProposalSectionDTO soSection;
        CommercialProposalAttachmentDTO soAttachment;

        TrxServiceOrder serviceOrderList = osRepo.findByProposalId(proposal.getId());

        if (serviceOrderList != null) {
            soSection = new CommercialProposalSectionDTO();
            soSection.setId(serviceOrderList.getIdServiceOrder());
            soSection.setSectionType("ORDEN_SERVICIO");
            soSection.setContent(serviceOrderList.getComments());
            soSection.setEnabled(true);

            sectionList.add(soSection);

            soAttachment = new CommercialProposalAttachmentDTO();
            soAttachment.setId(serviceOrderList.getIdServiceOrder());
            soAttachment.setSectionTypeId(SectionType.ORDEN_SERVICIO.ordinal());
            soAttachment.setSectionTypeName("ORDEN_SERVICIO");
            soAttachment.setStoredName(serviceOrderList.getFileName());
            soAttachment.setFilePath(serviceOrderList.getFilePath());
            soAttachment.setFileSizeMb(BigDecimal.ZERO);
            soAttachment.setMimeType("application/pdf");
            soAttachment.setOriginalName(serviceOrderList.getOsFolio());

            attachmentList.add(soAttachment);
        }

        for (CommercialProposalSectionDTO cps : proposalDTO.getSections()) {
            sectionList.add(cps);
        }

        for (CommercialProposalAttachmentDTO cpa : proposalDTO.getAttachments()) {
            attachmentList.add(cpa);
        }

        CommercialProposalDetailDTO proposalDTOFinal = new CommercialProposalDetailDTO();
        proposalDTOFinal.setId(proposalDTO.getId());
        proposalDTOFinal.setProposalFolio(proposalDTO.getProposalFolio());
        proposalDTOFinal.setServiceRequestId(proposalDTO.getServiceRequestId());
        proposalDTOFinal.setServiceRequestFolio(proposalDTO.getServiceRequestFolio());
        proposalDTOFinal.setStatusId(proposalDTO.getStatusId());
        proposalDTOFinal.setStatusName(proposalDTO.getStatusName());
        proposalDTOFinal.setDescription(proposalDTO.getDescription());
        proposalDTOFinal.setAssumptions(proposalDTO.getAssumptions());
        proposalDTOFinal.setInternalComments(proposalDTO.getInternalComments());
        proposalDTOFinal.setTotalProjectCost(proposalDTO.getTotalProjectCost());
        proposalDTOFinal.setSentToClientAt(proposalDTO.getSentToClientAt());
        proposalDTOFinal.setExpiresAt(proposalDTO.getExpiresAt());
        proposalDTOFinal.setCreatedAt(proposalDTO.getCreatedAt());
        proposalDTOFinal.setActive(proposalDTO.getActive());
        proposalDTOFinal.setRoles(proposalDTO.getRoles());
        proposalDTOFinal.setSections(sectionList);
        proposalDTOFinal.setAttachments(attachmentList);

        return proposalDTOFinal;
    }

    @Override
    @Transactional
    public CommercialProposalStatusChangeResponseDTO changeStatus(
            Long proposalId,
            CommercialProposalStatusChangeRequestDTO request,
            String username
    ) {

        CommercialProposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("CommercialProposal", "id", proposalId)
                );

        CatProposalStatus currentStatus = proposal.getStatus();

        // Regla: estatus finales no permiten cambios
        /*if (isFinalStatus(currentStatus)) {
            throw new IllegalStateException("La propuesta ya se encuentra cerrada");
        }*/

        CatProposalStatus newStatus = catProposalStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new IllegalArgumentException("Estatus no válido"));

        // Validación: comentario obligatorio si es Rechazado
        if (newStatus.getName().equalsIgnoreCase(ProposalStatusEnum.REJECTED.getName())
                && (request.getComment() == null || request.getComment().isBlank())) {
            throw new IllegalArgumentException("El comentario es obligatorio para rechazo");
        }

        Long previousStatusId = currentStatus.getId();

        //  Cambio de estatus de la propuesta 
        proposal.setStatus(newStatus);
        proposal.setUpdatedBy(username);
        proposal.setUpdatedAt(LocalDateTime.now());

        if (request.getComment() != null && !request.getComment().isBlank()) {
            proposal.setComments(request.getComment());
        }

        proposalRepository.save(proposal);

        //  HU04 / HU01 
        // Si la propuesta es Aceptada → el proyecto pasa a ABIERTO
        if (newStatus.getName().equalsIgnoreCase(ProposalStatusEnum.ACCEPTED_BY_CLIENT.getName())) {

            ServiceRequest sr = proposal.getServiceRequest();
            Sort sort = Sort.by(Sort.Direction.ASC, "name");
            CatProjectStatus abiertoStatus
                    = catProjectStatusRepository.findByName(ProjectStatusEnum.ABIERTO.getName(), sort)
                            .orElseThrow(()
                                    -> new IllegalStateException("Estatus ABIERTO no configurado")
                            );

            sr.setGeneralStatus(abiertoStatus);
            sr.setUpdatedBy(username);
            sr.setUpdatedAt(LocalDateTime.now());

            serviceRequestRepository.save(sr);

            emailService.sendCorreoProyectoNuevo(proposal);

        }

        return CommercialProposalStatusChangeResponseDTO.builder()
                .proposalId(proposal.getId())
                .previousStatusId(previousStatusId)
                .newStatusId(newStatus.getId())
                .newStatusName(newStatus.getName())
                .changedAt(LocalDateTime.now())
                .build();

    }

    private boolean isFinalStatus(CatProposalStatus status) {
        return status.getName().equalsIgnoreCase(ProposalStatusEnum.ACCEPTED_BY_CLIENT.getName())
                || status.getName().equalsIgnoreCase(ProposalStatusEnum.REJECTED.getName());
    }

    @Override
    @Transactional
    public void sendProposalToClient(Long proposalId, List<String> correos, String username) {

        CommercialProposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                "CommercialProposal", "id", proposalId
        ));

        /*if (proposal.getSentToClientAt() != null) {
            throw new BusinessException(
                    "La propuesta ya fue enviada al cliente"
            );
        }*/
        if (!proposal.getStatus().getName()
                .equalsIgnoreCase(ProposalStatusEnum.ACCEPTED_BY_CLIENT.getName())) {
            throw new BusinessException(
                    "Solo se puede enviar la propuesta si está ACEPTADA POR EL CLIENTE"
            );
        }

        /*LocalDateTime now = LocalDateTime.now();

        if (proposal.getExpiresAt() == null) {
            proposal.setExpiresAt(now.plusDays(30));
        }

        proposal.setUpdatedAt(now);
        proposal.setUpdatedBy(username);
        proposal.setSentToClientAt(now);

        proposalRepository.saveAndFlush(proposal);

        eventPublisher.publishEvent(
                new ProposalSentToClientEvent(proposal.getId())
        );*/
        ServiceRequest serviceRequest = serviceRequestRepository.findById(proposal.getServiceRequest().getId())
                .orElseThrow(() -> new IllegalArgumentException("Service Request no existe"));

        ByteArrayResource pdfPC = pdfService.downloadPDF(proposal, serviceRequest);

        emailService.sendProposalToClientAttachment(proposal, pdfPC, correos);

    }

    @Transactional
    public void sendExpirationReminder(CommercialProposal proposal) {

        LocalDateTime now = LocalDateTime.now();

        if (!proposal.getStatus().getName()
                .equalsIgnoreCase(ProposalStatusEnum.IN_REVIEW_BY_CLIENT.getName())) {
            return;
        }

        if (proposal.getReminderSentAt() != null) {
            return;
        }

        if (proposal.getExpiresAt() == null) {
            return;
        }

        LocalDateTime reminderFrom = proposal.getExpiresAt().minusDays(3);

        if (now.isBefore(reminderFrom) || now.isAfter(proposal.getExpiresAt())) {
            return;
        }

        emailService.sendTemplateEmail(
                proposal.getCreatedBy(),
                "Notificación de propuesta por expirar - "
                + proposal.getServiceRequest().getProjectName(),
                "proposal-expiring.html",
                Map.of(
                        "projectName", proposal.getServiceRequest().getProjectName(),
                        "proposalFolio", proposal.getProposalFolio()
                )
        );

        proposal.setReminderSentAt(now);
        proposalRepository.save(proposal);
    }

    @Override
    public ByteArrayResource downloadPDF(Long propuestaId) {
        CommercialProposal proposal = proposalRepository.findById(propuestaId)
                .orElseThrow(()
                        -> new ResourceNotFoundException("CommercialProposal", "id", propuestaId)
                );

        ServiceRequest serviceRequest = serviceRequestRepository.findById(proposal.getServiceRequest().getId())
                .orElseThrow(() -> new IllegalArgumentException("Service Request no existe"));

        return pdfService.downloadPDF(proposal, serviceRequest);
    }

    @Override
    public CommercialProposalResponseDTO update(
            CommercialProposalUpdateRequestDTO request,
            String currentUser
    ) {

        List<CatHoraAsignacionDTO> horas = horaService.getHorasAsignacion();

        ServiceRequest project = serviceRequestRepository.findById(request.getServiceRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no existe"));

        CommercialProposal proposalAnt = proposalRepository.findById(request.getIdProposal())
                .orElseThrow(() -> new ResourceNotFoundException("CommercialProposal", "id", request.getIdProposal()));

        CatProposalStatus currentStatus = proposalAnt.getStatus();

        // Regla: estatus finales no permiten cambios
        /*if (isFinalStatus(currentStatus)) {
            throw new IllegalStateException("La propuesta ya se encuentra cerrada");
        }*/

        CatProposalStatus initialStatus = catProposalStatusRepository
                .findById(request.getStatus())
                .orElseThrow(() -> new IllegalArgumentException("Status no encontrado"));

        CommercialProposal proposal = CommercialProposal.builder()
                .id(request.getIdProposal())
                .serviceRequest(project)
                .proposalFolio(request.getProvisionalFolio())
                .description(request.getDescription())
                .assumptions(request.getAssumptions())
                .internalComments(request.getInternalComments())
                .status(initialStatus)
                .active(true)
                .createdBy(proposalAnt.getCreatedBy())
                .createdAt(proposalAnt.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .updatedBy(currentUser)
                .build();

        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        /*List<CommercialProposalRole> rolesExistentes = rolesRepository.findByProposal(proposalAnt, sort);
        for (CommercialProposalRole cp : rolesExistentes) {
            rolesRepository.deleteById(cp.getId());
        }*/
        proposal.setRoles(request.getRoles().stream()
                .map(r -> buildRoleEntityUpdate(r, proposal, horas, currentUser))
                .toList());

        proposal.setSections(request.getSections().stream()
                .map(s -> CommercialProposalSection.builder()
                .id(s.getId())
                .proposal(proposal) 
                .sectionType(s.getSectionType())
                .content(s.getContent())
                .enabled(true)
                .updatedAt(LocalDateTime.now())
                .updatedBy(currentUser)
                .createdBy(proposalAnt.getCreatedBy())
                .createdAt(proposalAnt.getCreatedAt())
                .build())
                .toList());

        proposal.setTotalProjectCost(
                proposal.getRoles().stream()
                        .map(CommercialProposalRole::getTotalRoleCost)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        proposal.setAttachments(proposalAnt.getAttachments());

        proposal.setComments(proposalAnt.getComments());

        CommercialProposal saved = proposalRepository.save(proposal);

        return CommercialProposalResponseDTO.builder()
                .id(saved.getId())
                .proposalFolio(saved.getProposalFolio())
                .serviceRequestId(project.getId())
                .statusId(saved.getStatus().getId())
                .totalProjectCost(saved.getTotalProjectCost())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private CommercialProposalRole buildRoleEntityUpdate(
            CommercialProposalRoleDTO r,
            CommercialProposal proposal,
            List<CatHoraAsignacionDTO> horas,
            String user
    ) {

        Long id = 0L;
        BigDecimal totalHoras = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);

        CatRole role = catRoleRepository.findById(r.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no existe"));

        if (r.getId() != 0) {
            id = r.getId();
        } else {
            id = null;
        }

        CatHoraAsignacionDTO hora = horas.stream().filter(h -> h.getTipoAsignacion().equals(r.getAssignmentType()) && h.getPorcentaje() == r.getAssignmentPercentage().longValue()).findAny().orElse(null);

        if (hora != null) {
            totalHoras = hora.getValor();
            total = totalHoras
                    .multiply(r.getMonths())
                    .multiply(BigDecimal.valueOf(r.getQuantity()))
                    .multiply(r.getHourlyRate());
        } else {
            totalHoras = BigDecimal.valueOf(1);
            total = totalHoras
                    .multiply(r.getMonths())
                    .multiply(BigDecimal.valueOf(r.getQuantity()))
                    .multiply(r.getHourlyRate())
                    .multiply(r.getAssignmentPercentage().divide(BigDecimal.valueOf(100)));
        }

        return CommercialProposalRole.builder()
                .id(id)
                .proposal(proposal)
                .role(role)
                .typeDeveloper(r.getTypeDeveloper())
                .quantity(r.getQuantity())
                .hourlyRate(r.getHourlyRate())
                .assignmentPercentage(r.getAssignmentPercentage())
                .assignmentType(r.getAssignmentType())
                .months(r.getMonths())
                .totalRoleCost(total)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public CommercialProposalSectionDTO addSection(
            Long id,
            CommercialProposalSectionDTO request,
            String currenUser
    ) {
        CommercialProposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommercialProposal", "id", id));

        CommercialProposalSection section = CommercialProposalSection.builder()
                .id(request.getId())
                .proposal(proposal)
                .sectionType(request.getSectionType())
                .content(request.getContent())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .createdBy(currenUser)
                .build();

        CommercialProposalSection sec = sectionRepository.save(section);

        CommercialProposalSectionDTO newSection = new CommercialProposalSectionDTO();
        newSection.setId(sec.getId());
        newSection.setSectionType(sec.getSectionType());
        newSection.setContent(sec.getContent());
        newSection.setEnabled(sec.getEnabled());

        return newSection;
    }

    @Override
    public CommercialProposalSectionDTO updateOrderService(
            Long id,
            CommercialProposalSectionDTO request,
            String currentUser
    ) {
        TrxServiceOrder serviceOrderNueva = new TrxServiceOrder();
        CommercialProposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommercialProposal", "id", id));

        if (request.getId() > 0L) {
            TrxServiceOrder serviceOrder = osRepo.findById(request.getId()).get();

            serviceOrderNueva = TrxServiceOrder.builder()
                    .idServiceOrder(request.getId())
                    .proposal(proposal)
                    .osFolio(serviceOrder.getOsFolio())
                    .fileName(serviceOrder.getFileName())
                    .filePath(serviceOrder.getFilePath())
                    .comments(request.getContent())
                    .createdBy(serviceOrder.getCreatedBy())
                    .createdAt(serviceOrder.getCreatedAt())
                    .updatedBy(currentUser)
                    .updatedAt(LocalDateTime.now())
                    .active(true)
                    .build();
        } else {
            serviceOrderNueva = TrxServiceOrder.builder()
                    .proposal(proposal)
                    .osFolio("OS_" + proposal.getProposalFolio())
                    .fileName(null)
                    .filePath(null)
                    .comments(request.getContent())
                    .createdBy(currentUser)
                    .createdAt(LocalDateTime.now())
                    .active(true)
                    .build();
        }
        osRepo.save(serviceOrderNueva);

        return CommercialProposalSectionDTO.builder()
                .id(id)
                .sectionType(currentUser)
                .content(currentUser)
                .enabled(Boolean.FALSE)
                .build();

    }

    private boolean validarExisteOS(Long proposalId) {
        TrxServiceOrder serviceOrder = osRepo.findByProposalId(proposalId);

        return (serviceOrder != null);
    }

}
