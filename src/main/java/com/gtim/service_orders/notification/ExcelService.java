package com.gtim.service_orders.notification;

import com.gtim.service_orders.dto.ColaboradorDTO;
import com.gtim.service_orders.dto.CoordinadoresDTO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ExcelService {
    ByteArrayInputStream crearReporteErroresCoordinador(List<CoordinadoresDTO> data) throws IOException;
    ByteArrayInputStream crearReporteErroresColaborador(List<ColaboradorDTO> data) throws IOException;
    
}
