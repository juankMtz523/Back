package com.gtim.service_orders.notification;

import com.gtim.service_orders.entity.CommercialProposal;
import com.gtim.service_orders.entity.ServiceRequest;
import org.springframework.core.io.ByteArrayResource;

public interface PDFService {
    public ByteArrayResource downloadPDF(CommercialProposal proposal, ServiceRequest serviceRequest);
}
