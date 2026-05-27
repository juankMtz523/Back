package com.gtim.service_orders.service;

import java.util.List;
import com.gtim.service_orders.dto.CatCoordinatorDTO;
import com.gtim.service_orders.dto.CoordinatorRequestDTO;
import com.gtim.service_orders.dto.ResourceDeleteDTO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface CatCoordinatorService {

    List<CatCoordinatorDTO> getActiveCoordinators();

    CoordinatorRequestDTO create(CoordinatorRequestDTO request, String user);

    CoordinatorRequestDTO update(CoordinatorRequestDTO request, String user);

    ByteArrayInputStream descargarPlantilla();
    
    void cargaMasivaCoordinadores(MultipartFile file, String user);
    
    ResourceDeleteDTO getColaboradorAEliminar(Long resourceID, Long roleId);
    
    List<String> desactivarCoordinador(Long resourceIdAnt, Long roleId, Long resourceIdNew, String user);
}
