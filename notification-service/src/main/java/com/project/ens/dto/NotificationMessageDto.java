package com.project.ens.dto;

import com.project.ens.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto {
    private String recipient;
    private String message;
    private NotificationType type;
}
