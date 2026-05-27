package com.gtim.service_orders.notification;

import java.util.Map;

import com.gtim.service_orders.entity.CommercialProposal;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;

public interface EmailService {

    void sendProposalExpiringReminder(CommercialProposal proposal);

    void sendProposalToClient(CommercialProposal proposal);
    
    void sendProposalToClientAttachment(CommercialProposal proposal, ByteArrayResource adjunto, List<String> correos);

    void sendTemplateEmail(String createdBy, String string, String string2, Map<String, String> of);

    void sendGeneralNotification();
    
    void sendCorreoProyectoNuevo(CommercialProposal proposal);
    
    void sendErroresCargaMasiva(String titulo, String userName, ByteArrayResource adjunto);
}
