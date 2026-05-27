package com.gtim.service_orders.dao;

import com.gtim.service_orders.dto.CorreoCoordinadorDTO;
import com.gtim.service_orders.dto.MensajesCorreoDTO;
import java.util.List;

public interface MensajesCorreoDAO {
    List<MensajesCorreoDTO> getMensajesCorreo();
    List<CorreoCoordinadorDTO> getCoordinadoresProyectoNuevo(Long proposalId);
    void setRechazadosPropuestasX30Dias();
}
