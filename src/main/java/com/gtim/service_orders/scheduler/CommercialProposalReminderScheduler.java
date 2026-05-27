package com.gtim.service_orders.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gtim.service_orders.service.CommercialProposalReminderService;
import com.gtim.service_orders.notification.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommercialProposalReminderScheduler {

    private final CommercialProposalReminderService reminderService;
    private final EmailService emailService;

    @Scheduled(cron = "${scheduler.proposals.reminder-cron}")
    public void processProposalReminders() {
        log.info("Iniciando scheduler de recordatorios de propuestas");
        emailService.sendGeneralNotification();
        log.info("Scheduler de propuestas finalizado");
    }
}
