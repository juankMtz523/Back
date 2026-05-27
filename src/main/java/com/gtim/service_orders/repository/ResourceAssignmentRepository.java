package com.gtim.service_orders.repository;

import com.gtim.service_orders.entity.TrxResourceAssignment;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceAssignmentRepository extends JpaRepository<TrxResourceAssignment, Long> {

    @Query(value = "SELECT public.get_fecha_final(:p_fechainicio, :p_diasagregar)")
    LocalDate callFunctionFechaFinal(@Param("p_fechainicio") LocalDate p_fechainicio, @Param("p_diasagregar") Long p_diasagregar);

    @Query(value = "SELECT public.get_fecha_final_2(:p_tipoasignacion, :p_porcentajeasignacion, :p_cantidadasignacion, :p_fechainicio)")
    LocalDate callFunctionFechaFinal2(@Param("p_tipoasignacion") String p_tipoasignacion, @Param("p_porcentajeasignacion") Long p_porcentajeasignacion, @Param("p_cantidadasignacion") Long p_cantidadasignacion, @Param("p_fechainicio") LocalDate p_fechainicio);
    
    @Query(value = "SELECT public.get_total_horas_cumplidos(:p_fechainicio, :p_porcentajeasignacion)")
    Long callFunctionHorasCumplidas(@Param("p_fechainicio") LocalDate p_fechainicio, @Param("p_porcentajeasignacion") Long p_porcentajeasignacion);

    @Query(value = "SELECT public.get_total_horas(:p_tipoasignacion, :p_tiempo)")
    Long callFunctionTotalHoras(@Param("p_tipoasignacion") String p_tipoasignacion, @Param("p_tiempo") Long p_tiempo);
    
    @Query(value = "SELECT public.get_pct_asignacion_resource(:p_resource, :p_roleid)")
    Long callFunctionAsignacionResource(@Param("p_resource") Long p_resource, @Param("p_roleid") Long p_roleid);

    Long countByResourceIdAndRoleIdAndEndDateAfter(Long resourceId, Long roleId, LocalDate date);
}
