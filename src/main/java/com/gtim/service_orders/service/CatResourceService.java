package com.gtim.service_orders.service;

import com.gtim.service_orders.dto.CatResourceDTO;
import com.gtim.service_orders.dto.ResourceDeleteDTO;
import com.gtim.service_orders.dto.ResourceRequestDTO;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CatResourceService {

    List<CatResourceDTO> getResourcesNotAssignProposal(Long proposalId);
    
    List<CatResourceDTO> getActiveResources();

    ResourceRequestDTO create(ResourceRequestDTO request, String user);

    ResourceRequestDTO update(ResourceRequestDTO request, String user);

    ByteArrayInputStream descargarPlantilla();

    void cargaMasivaColaboaradores(MultipartFile file, String user);

    ResourceDeleteDTO getColaboradorAEliminar(Long resourceID, Long roleId);
    
    List<String> desactivarColaborador(Long resourceIdAnt, String user);
}
