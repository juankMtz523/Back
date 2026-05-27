package com.gtim.service_orders.service;

import com.gtim.service_orders.dto.ProjectAutocompleteDTO;
import com.gtim.service_orders.dto.ProjectDashboardDTO;
import com.gtim.service_orders.dto.ServiceRequestDTO;
import com.gtim.service_orders.dto.ServiceRequestDetailDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceRequestService {

    ServiceRequestDTO create(ServiceRequestDTO dto, String currentUser);

    ServiceRequestDTO findById(Long id);

    List<ServiceRequestDTO> findAll(String currenUser);

    void delete(Long id);

    ServiceRequestDTO update(Long id, ServiceRequestDTO dto, String currentUser);

    List<ProjectAutocompleteDTO> autocompleteProjects(String query);

    Page<ProjectDashboardDTO> getDashboard(Pageable pageable);

    ServiceRequestDetailDTO findDetailById(Long id);

}
