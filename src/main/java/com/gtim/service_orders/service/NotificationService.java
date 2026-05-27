package com.gtim.service_orders.service;

public interface NotificationService {
    void notifyChange(Long proposalId, String subject, String message);
}
