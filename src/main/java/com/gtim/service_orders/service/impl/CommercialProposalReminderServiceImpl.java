package com.gtim.service_orders.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.entity.CatProposalStatus;
import com.gtim.service_orders.entity.CommercialProposal;
import com.gtim.service_orders.enums.ProposalStatusEnum;
import com.gtim.service_orders.notification.EmailService;
import com.gtim.service_orders.repository.CatProposalStatusRepository;
import com.gtim.service_orders.repository.CommercialProposalRepository;
import com.gtim.service_orders.service.CommercialProposalReminderService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class CommercialProposalReminderServiceImpl implements CommercialProposalReminderService {

	private final CommercialProposalRepository commercialProposalRepository;
	private final EmailService emailService;
	private final CatProposalStatusRepository catProposalStatusRepository;
	

	private static final Logger log = LoggerFactory.getLogger(CommercialProposalReminderServiceImpl.class);
	private static final int REMINDER_DAYS_BEFORE_EXPIRATION = 3;

	@Override
	@Transactional
	public void sendExpirationReminders() {

		LocalDate today = LocalDate.now();

		// Propuestas enviadas, aceptadas por el cliente y sin recordatorio previo
		List<CommercialProposal> proposals = commercialProposalRepository
				.findForExpirationReminder(ProposalStatusEnum.ACCEPTED_BY_CLIENT.getName());

		for (CommercialProposal proposal : proposals) {

			if (proposal.getSentToClientAt() == null || proposal.getExpiresAt() == null) {
				continue;
			}

			long daysElapsed = ChronoUnit.DAYS.between(proposal.getSentToClientAt().toLocalDate(), today);

			long reminderDay = ChronoUnit.DAYS.between(proposal.getSentToClientAt().toLocalDate(),
					proposal.getExpiresAt().toLocalDate()) - REMINDER_DAYS_BEFORE_EXPIRATION;

			if (daysElapsed == reminderDay && proposal.getReminderSentAt() == null) {

				try {
					emailService.sendProposalExpiringReminder(proposal);

					proposal.setReminderSentAt(LocalDateTime.now());
					commercialProposalRepository.save(proposal);

					log.info("Recordatorio enviado (faltan {} días) → Propuesta {}", REMINDER_DAYS_BEFORE_EXPIRATION,
							proposal.getProposalFolio());

				} catch (Exception ex) {
					log.error("Error enviando recordatorio de propuesta {}", proposal.getProposalFolio(), ex);
				}
			}
		}
	}
	
	@Override
    @Transactional
    public void expireProposals() {

        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.ASC,"name");

        CatProposalStatus rejectedStatus =
        		catProposalStatusRepository.findByName(ProposalStatusEnum.REJECTED.getName(), sort)
                        .orElseThrow(() ->
                                new IllegalStateException("Estatus RECHAZADO no configurado")
                        );

		List<CommercialProposal> expiredProposals = commercialProposalRepository.findExpiredProposals(now);

        for (CommercialProposal proposal : expiredProposals) {

            log.info("Cerrando propuesta expirada {}", proposal.getId());

            proposal.setStatus(rejectedStatus);
            proposal.setInternalComments(
                    "Rechazada por tiempo de respuesta del cliente"
            );
            proposal.setUpdatedAt(now);
            proposal.setUpdatedBy("SYSTEM");
        }

        commercialProposalRepository.saveAll(expiredProposals);
    }

}
