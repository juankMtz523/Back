package com.gtim.service_orders.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.gtim.service_orders.event.ProposalSentToClientEvent;
import com.gtim.service_orders.notification.EmailService;
import com.gtim.service_orders.repository.CommercialProposalRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProposalEmailListener {

    private final CommercialProposalRepository proposalRepository;
    private final EmailService emailService;

	@EventListener
	public void onProposalAccepted(ProposalSentToClientEvent event) {

		proposalRepository.findById(event.proposalId()).ifPresent(emailService::sendProposalToClient);
	}
}
