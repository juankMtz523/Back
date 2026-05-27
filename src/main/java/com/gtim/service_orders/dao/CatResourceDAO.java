package com.gtim.service_orders.dao;

import com.gtim.service_orders.dto.CatResourceDTO;
import com.gtim.service_orders.dto.ColaboradoresCoordinadorDTO;
import com.gtim.service_orders.dto.ProyectosColaboradorDTO;
import java.util.List;

public interface CatResourceDAO {

    public List<ProyectosColaboradorDTO> getProyectosXColaborador(Long roleId, Long resourceId);

    public List<ColaboradoresCoordinadorDTO> getColaboradoresXCoordinador(Long coordinadorId);
    
    public List<CatResourceDTO> getResourceXProposal(Long proposalId);
    
    public List<String> desactivarCoordinador(Long resourceIdAnt, Long roleId, Long resourceIdNew, String user);
    
    public List<String> desactivarColaborador(Long resourceIdAnt, String user);
}
