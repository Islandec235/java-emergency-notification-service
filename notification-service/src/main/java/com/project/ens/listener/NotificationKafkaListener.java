package com.project.ens.listener;

import com.project.ens.dto.NotificationMessageDto;
import com.project.ens.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaListener {
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${kafka.topics.notification}",
            groupId = "${kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleNotification(NotificationMessageDto notificationMessage) {
        log.info("Получено сообщение с уведомлением: {}", notificationMessage);
        notificationService.processNotification(notificationMessage);
    }
}
