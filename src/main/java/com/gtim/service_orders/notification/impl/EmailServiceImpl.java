package com.gtim.service_orders.notification.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.gtim.service_orders.entity.CatClient;
import com.gtim.service_orders.entity.CommercialProposal;
import com.gtim.service_orders.security.entity.User;
import com.gtim.service_orders.exception.BusinessException;
import com.gtim.service_orders.notification.EmailService;

import com.gtim.service_orders.security.repository.UserRepository;

import com.gtim.service_orders.dao.MensajesCorreoDAO;
import com.gtim.service_orders.dto.CorreoCoordinadorDTO;
import com.gtim.service_orders.dto.MensajesCorreoDTO;
import com.gtim.service_orders.entity.CommercialProposalRole;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.FileCopyUtils;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MensajesCorreoDAO mensaje;
    private final UserRepository userRepo;

    Locale locale = new Locale.Builder().setLanguage("es").setRegion("MX").build();
    DateTimeFormatter longMxFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(locale);

    /**
     * Método genérico para leer plantilla HTML y reemplazar placeholders.
     */
    private String getTemplate(String templateFile, Map<String, String> placeholders) {
        try {

            InputStream resourceIS = new ClassPathResource("email-templates/" + templateFile).getInputStream();
            Resource resource = new InputStreamResource(resourceIS);

            Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            String content = FileCopyUtils.copyToString(reader);

            /*Resource resource = new ClassPathResource("email-templates/" + templateFile);
            String content = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);*/
            if (placeholders != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
                }
            }

            return content;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer plantilla de correo: " + templateFile, e);
        }
    }

    private void sendEmailAttachment(String to, String subject, String htmlContent, ByteArrayResource adjunto, String folio) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.addAttachment("PC-" + folio + ".pdf", adjunto, "application/pdf");
            helper.setFrom("notificacion@gtim.mx");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo con adjunto a " + to, e);
        }
    }

    private void sendEmailAttachmentCargas(String to, String subject, String htmlContent, ByteArrayResource adjunto, String nombreArchivo) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.addAttachment(nombreArchivo, adjunto, "application/vnd.ms-excel");
            helper.setFrom("notificacion@gtim.mx");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo con adjunto a " + to, e);
        }
    }    
    
    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo a " + to, e);
        }
    }

    public void sendTemplateEmail(
            String to,
            String subject,
            String template,
            Map<String, String> placeholders
    ) {
        String html = getTemplate(template, placeholders);
        sendEmail(to, subject, html);
    }

    public void sendTemplateEmailAttachment(
            String to,
            String subject,
            String template,
            Map<String, String> placeholders,
            ByteArrayResource adjunto,
            String folio
    ) {
        String html = getTemplate(template, placeholders);
        sendEmailAttachment(to, subject, html, adjunto, folio);
    }

    @Override
    public void sendProposalToClientAttachment(CommercialProposal proposal, ByteArrayResource adjunto, List<String> correos) {

        if (!correos.isEmpty()) {

            CatClient client = proposal.getServiceRequest().getClient();

            Map<String, String> placeholders = Map.of(
                    "clientName", client.getName(),
                    "projectName", proposal.getServiceRequest().getProjectName(),
                    "proposalFolio", proposal.getProposalFolio(),
                    "expirationDate",
                    proposal.getCreatedAt().plusDays(30).toLocalDate().format(longMxFormatter)
            );
            for (String email : correos) {
                sendTemplateEmailAttachment(
                        email,
                        "Propuesta comercial aceptada",
                        "proposal-accepted.html",
                        placeholders,
                        adjunto,
                        proposal.getProposalFolio()
                );
            }
        } else {
            throw new BusinessException("Se ocupa el correo para enviar la propuesta");
        }

    }

    @Override
    public void sendProposalToClient(CommercialProposal proposal) {

        CatClient client = proposal.getServiceRequest().getClient();

        String email = /*client.getEmail();*/ "carlos.martinez@gtim.mx";
        if (email == null || email.isBlank()) {
            throw new BusinessException(
                    "El cliente no tiene correo configurado para recibir la propuesta"
            );
        }

        Map<String, String> placeholders = Map.of(
                "clientName", client.getName(),
                "projectName", proposal.getServiceRequest().getProjectName(),
                "proposalFolio", proposal.getProposalFolio(),
                "expirationDate",
                proposal.getExpiresAt().toLocalDate().toString()
        );

        sendTemplateEmail(
                email,
                "Propuesta comercial aceptada",
                "proposal-accepted.html",
                placeholders
        );
    }

    @Override
    public void sendProposalExpiringReminder(CommercialProposal proposal) {

        String email = proposal.getServiceRequest()
                .getClient()
                .getEmail();

        if (email == null || email.isBlank()) {
            throw new BusinessException(
                    "El cliente no tiene correo configurado para recibir recordatorios"
            );
        }

        Map<String, String> placeholders = Map.of(
                "clientName",
                proposal.getServiceRequest().getClient().getName(),
                "projectName",
                proposal.getServiceRequest().getProjectName(),
                "proposalFolio",
                proposal.getProposalFolio(),
                "expirationDate",
                proposal.getExpiresAt().toLocalDate().toString()
        );

        sendTemplateEmail(
                email,
                "Propuesta por expirar - " + proposal.getServiceRequest().getProjectName(),
                "proposal-expiring.html",
                placeholders
        );
    }

    @Override
    public void sendErroresCargaMasiva(String titulo, String userName, ByteArrayResource adjunto) {
        Map<String, String> placeholders;
        String body;
       
        User u = userRepo.findByEmail(userName).get();
                
        placeholders = Map.of("tituloCorreo", "Resultados Carga Masiva de " + titulo,
                "tipoCorreo", "Carga Masiva de " + titulo,
                "clientName", u.getFirstName() + " " + u.getLastName(),
                "mensaje", "Se adjunta la lista de " + titulo + " que no se pudieron dar de alta."
        );
        body = getTemplate("general-notification.html", placeholders);

        this.sendEmailAttachmentCargas(u.getEmail(), "Resultados Carga Masiva de " + titulo, body, adjunto, titulo+".xlsx");
    }

    @Override
    public void sendGeneralNotification() {
        List<MensajesCorreoDTO> listadoMensajes = mensaje.getMensajesCorreo();
        Map<String, String> placeholders;
        String body;
        int contador = 0;

        for (MensajesCorreoDTO mc : listadoMensajes) {
            contador++;
            if (mc.getTitulo() != null && !mc.getTitulo().equals("NA")) {
                if (mc.getCorreoCoordinador() != null) {
                    placeholders = Map.of("tituloCorreo", mc.getTitulo(),
                            "tipoCorreo", mc.getTitulo(),
                            "clientName", mc.getNombreCoordinador(),
                            "mensaje", mc.getMensajeCoordinador()
                    );
                    body = getTemplate("general-notification.html", placeholders);
                    //sendEmail(mc.getCorreoCoordinador(), mc.getTitulo(), body);
                    sendEmail(/*mc.getCorreoCoordinador()*/"carlos.martinez@gtim.mx", mc.getTitulo(), body);
                    sendEmail(/*mc.getCorreoCoordinador()*/"sergio.guereca@gtim.mx", mc.getTitulo(), body);
                    placeholders = null;

                    if (mc.getCorreoColaborador() != null) {
                        placeholders = Map.of("tituloCorreo", mc.getTitulo(),
                                "tipoCorreo", mc.getTitulo(),
                                "clientName", mc.getNombreColaborador(),
                                "mensaje", mc.getMensajeColaborador()
                        );
                        body = getTemplate("general-notification.html", placeholders);
                        //sendEmail(mc.getCorreoColaborador(), mc.getTitulo(), body);
                        sendEmail(/*mc.getCorreoCoordinador()*/"carlos.martinez@gtim.mx", mc.getTitulo(), body);
                        sendEmail(/*mc.getCorreoCoordinador()*/"sergio.guereca@gtim.mx", mc.getTitulo(), body);
                        placeholders = null;
                    }
                }
            }
        }

        mensaje.setRechazadosPropuestasX30Dias();
    }

    @Override
    public void sendCorreoProyectoNuevo(CommercialProposal proposal) {
        List<CorreoCoordinadorDTO> correos = mensaje.getCoordinadoresProyectoNuevo(proposal.getId());
        Map<String, String> placeholders;
        String body;
        StringBuilder filasRoles = new StringBuilder();

        for (CommercialProposalRole cpr : proposal.getRoles()) {
            filasRoles.append("<tr style=\"font-size: x-small\">");
            filasRoles.append("<td style=\"text-align: left;\">").append(cpr.getRole().getDescription());
            if (cpr.getTypeDeveloper() != null) {
                filasRoles.append(" ").append(cpr.getTypeDeveloper());
            }
            filasRoles.append("</td>");
            filasRoles.append("<td style=\"text-align: center\">").append(cpr.getQuantity()).append("</td>");
            filasRoles.append("<td style=\"text-align: center\">").append(cpr.getAssignmentPercentage()).append("%").append("</td>");
            filasRoles.append("<td style=\"text-align: center\">").append(cpr.getMonths()).append(" ").append(cpr.getAssignmentType()).append("</td>");
            filasRoles.append("</tr>");
        }

        for (CorreoCoordinadorDTO cc : correos) {
            placeholders = Map.of("coordinador", cc.getNombreCoordinador(),
                    "projectName", proposal.getServiceRequest().getProjectName(),
                    "clientName", proposal.getServiceRequest().getClient().getName(),
                    "clientFolio", "Proyect-" + proposal.getServiceRequest().getClienteFolio(),
                    "startDate", proposal.getServiceRequest().getTentativeStartDate().format(longMxFormatter),
                    "filas", filasRoles.toString()
            );
            body = getTemplate("new-project.html", placeholders);
            //sendEmail(cc.getCorreoCoordinador(), "Nuevo Proyecto", body);
            sendEmail(/*cc.getCorreoCoordinador()*/"carlos.martinez@gtim.mx", "Nuevo Proyecto", body);
            sendEmail(/*cc.getCorreoCoordinador()*/"sergio.guereca@gtim.mx", "Nuevo Proyecto", body);
            placeholders = null;
            break;
        }
    }

}
