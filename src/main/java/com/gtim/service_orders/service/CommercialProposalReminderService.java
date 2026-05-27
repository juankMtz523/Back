package com.gtim.service_orders.service;

public interface CommercialProposalReminderService {

	void sendExpirationReminders();

	void expireProposals();

}
