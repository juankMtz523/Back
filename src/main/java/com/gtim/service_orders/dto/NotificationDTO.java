package com.gtim.service_orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class NotificationDTO extends BaseAuditDTO {

    private Long id;
    private Long recipientCoordinatorId;
    private String recipientEmail;
    private String subject;
    private String message;
    private String sentBy;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime sentAt;
    private String status;
}
