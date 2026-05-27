package com.gtim.service_orders.notification.impl;

import com.gtim.service_orders.entity.CommercialProposal;
import com.gtim.service_orders.entity.ServiceRequest;
import com.gtim.service_orders.service.CommercialProposalAttachmentService;
import com.gtim.service_orders.notification.PDFService;

import com.gtim.service_orders.dto.ListadoEtapasDTO;
import com.gtim.service_orders.dto.ListadoRolesDTO;
import com.gtim.service_orders.entity.CommercialProposalAttachment;
import com.gtim.service_orders.entity.CommercialProposalRole;
import com.gtim.service_orders.entity.CommercialProposalSection;
import static com.gtim.service_orders.enums.SectionType.PLAN_ALTO_NIVEL;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {

    @Autowired
    private CommercialProposalAttachmentService attachmentService;

    Locale locale = new Locale.Builder().setLanguage("es").setRegion("MX").build();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy",locale);
    DateTimeFormatter longMxFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale);
    NumberFormat nf = NumberFormat.getCurrencyInstance(locale);    
    NumberFormat nfp = NumberFormat.getPercentInstance(locale);
    
    @Override
    public ByteArrayResource downloadPDF(CommercialProposal proposal, ServiceRequest serviceRequest) {
        byte[] exportarPDF = null;
        File file = null;
        InputStream resource = null;
        String nombreArchivo = null;
        List<ListadoEtapasDTO> listEtapas = new ArrayList<>();
        ListadoEtapasDTO etapa;
        List<ListadoRolesDTO> listRoles = new ArrayList<>();
        ListadoRolesDTO rol;
        String alcance = "", planAltoNivel = "", condicionesComerciales = "", supuestos = "";
        ByteArrayOutputStream planAltoNivelbaos = null;
        try {
            nombreArchivo = "somGTIMPDF.jasper";
            try {
                file = ResourceUtils.getFile("/app/" + nombreArchivo);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("No se encontre el template del pdf", e);
            }            
            if(serviceRequest.getMaturationStatus() != null){
                etapa = new ListadoEtapasDTO();
                etapa.setNombreEtapa("Maduración");
                etapa.setEstatusEtapa(serviceRequest.getMaturationStatus().getName());
                etapa.setFechaInicio(serviceRequest.getMaturationStartDate() != null ? serviceRequest.getMaturationStartDate().format(longMxFormatter) : "");
                etapa.setFechaFin(serviceRequest.getMaturationEndDate() != null ? serviceRequest.getMaturationEndDate().format(longMxFormatter) : "");
                
                listEtapas.add(etapa);
            }
            if(serviceRequest.getConstructionStatus() != null){
                etapa = new ListadoEtapasDTO();
                etapa.setNombreEtapa("Construcción");
                etapa.setEstatusEtapa(serviceRequest.getConstructionStatus().getName());
                etapa.setFechaInicio(serviceRequest.getConstructionStartDate() != null ? serviceRequest.getConstructionStartDate().format(longMxFormatter) : "");
                etapa.setFechaFin(serviceRequest.getConstructionEndDate() != null ? serviceRequest.getConstructionEndDate().format(longMxFormatter) : "");
                
                listEtapas.add(etapa);
            }            
            if(serviceRequest.getStabilizationStatus() != null){
                etapa = new ListadoEtapasDTO();
                etapa.setNombreEtapa("Estabilización");
                etapa.setEstatusEtapa(serviceRequest.getStabilizationStatus().getName());
                etapa.setFechaInicio(serviceRequest.getStabilizationStartDate() != null ? serviceRequest.getStabilizationStartDate().format(longMxFormatter) : "");
                etapa.setFechaFin(serviceRequest.getStabilizationEndDate() != null ? serviceRequest.getStabilizationEndDate().format(longMxFormatter) : "");
                
                listEtapas.add(etapa);
            }        
            BigDecimal pct = new BigDecimal(100);
            if(!proposal.getRoles().isEmpty()){
                 for(CommercialProposalRole r : proposal.getRoles()){
                     rol = new ListadoRolesDTO();
                     if(r.getRole().getName().toUpperCase().equals("DESARROLLADOR")){
                         rol.setRol(r.getRole().getDescription() + " " + r.getTypeDeveloper());
                     }else{
                         rol.setRol(r.getRole().getDescription());
                     }
                     rol.setCantidad(r.getQuantity());
                     rol.setTarifa(nf.format(r.getHourlyRate()));
                     rol.setAsignacion(nfp.format(r.getAssignmentPercentage().divide(pct)));
                     rol.setTiempo(r.getMonths() + " " + r.getAssignmentType());
                     rol.setCostoTotal(nf.format(r.getTotalRoleCost()));
                     
                     listRoles.add(rol);
                 }
            }
            if(!proposal.getSections().isEmpty()){
                for(CommercialProposalSection s : proposal.getSections()){
                    switch(s.getSectionType()){
                        case "ALCANCE" -> alcance = s.getContent();
                        case "PLAN_ALTO_NIVEL" -> planAltoNivel = s.getContent();
                        case "CONDICIONES_COMERCIALES" -> condicionesComerciales = s.getContent();
                        case "SUPUESTOS" -> supuestos = s.getContent();
                    }
                }
            }
            if(!proposal.getAttachments().isEmpty()){
                for(CommercialProposalAttachment a: proposal.getAttachments()){
                    switch(a.getSectionType()){
                        case PLAN_ALTO_NIVEL -> { 
                            planAltoNivelbaos = attachmentService.downloadStorage(a.getStoredName());
                        }
                    }
                }
            }
            final JasperReport report = (JasperReport) JRLoader.loadObject(file);
            final HashMap<String, Object> parameters = new HashMap<>();            
            BufferedImage imageBackground = ImageIO.read(getClass().getResource("/imagenes/fondo_gtim.png"));
            BufferedImage imageLogo = ImageIO.read(getClass().getResource("/imagenes/logo_gtim.png"));
            BufferedImage imageIcon = ImageIO.read(getClass().getResource("/imagenes/iconopdf.png"));
            parameters.put("pImageBackground", imageBackground);
            parameters.put("pImageLogo", imageLogo);
            parameters.put("pTituloProyecto", serviceRequest.getClienteFolio() != null ? "Proyecto - " + serviceRequest.getClienteFolio() : "");
            parameters.put("pNombreProyecto", serviceRequest.getProjectName() != null ? serviceRequest.getProjectName() : "");
            parameters.put("pDetalleNombreProyecto", serviceRequest.getProjectName() != null ? serviceRequest.getProjectName() + " - " + serviceRequest.getClienteFolio() : "");
            parameters.put("pCliente", serviceRequest.getClient() != null ? serviceRequest.getClient().getName() : "");
            parameters.put("pFolioProyecto", serviceRequest.getClienteFolio() != null ? serviceRequest.getClienteFolio() : "");
            String fechaInicio = serviceRequest.getTentativeStartDate() != null ? serviceRequest.getTentativeStartDate().format(longMxFormatter) : "";
            parameters.put("pFechaInicio", fechaInicio);
            parameters.put("pFolioPropuestaComercial", proposal.getProposalFolio() != null ? proposal.getProposalFolio() : "");
            parameters.put("pEstatusPropuestaComercial", proposal.getStatus() != null ? proposal.getStatus().getDescription() : "");
            parameters.put("pDescripcionPropuestaComercial", proposal.getDescription() != null ? proposal.getDescription() : "");
            parameters.put("pAlcancePropuestaComercial",alcance);
            if(planAltoNivelbaos != null){
                InputStream planAltoNivelis = new ByteArrayInputStream(planAltoNivelbaos.toByteArray());
                BufferedImage imagePlanAltoNivel = ImageIO.read(planAltoNivelis);
                parameters.put("pPlanAltaNivel", imagePlanAltoNivel);
            }            
            parameters.put("pPlanAltoNivelTxt", planAltoNivel);
            String totalPropuesta = proposal.getTotalProjectCost() != null ? nf.format(proposal.getTotalProjectCost()) : "";
            parameters.put("pTotalGeneral",totalPropuesta);
            parameters.put("pSupuestos",supuestos);
            parameters.put("pCondicionesGenerales",condicionesComerciales);
            if(!listEtapas.isEmpty()){
                JRBeanCollectionDataSource etapaJRBean = new JRBeanCollectionDataSource(listEtapas);
                parameters.put("CollectionBeanParamEtapas", etapaJRBean);
            }
            if(!listRoles.isEmpty()){
                JRBeanCollectionDataSource rolesJRBean = new JRBeanCollectionDataSource(listRoles);
                parameters.put("CollectionBeanParamRoles", rolesJRBean);
            }
            parameters.put("pHeaderIcon", imageIcon);
            LocalDate hoy = LocalDate.now();
            String fechaActual = hoy.format(longMxFormatter);
            parameters.put("pFechaActual", fechaActual);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            exportarPDF = JasperExportManager.exportReportToPdf(jasperPrint);            
        } catch (Exception e) {
            throw new RuntimeException("Error al leer plantilla de pdf", e);
        }
        return new ByteArrayResource(exportarPDF);
    }

}
