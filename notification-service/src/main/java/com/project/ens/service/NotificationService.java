package com.project.ens.service;

import com.project.ens.dto.NotificationMessageDto;

public interface NotificationService {
    void processNotification(NotificationMessageDto messageDto);
}
