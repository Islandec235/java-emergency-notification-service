package com.project.ens.listener;

import com.project.ens.dto.NotificationMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DLQConsumer {
    @KafkaListener(topics = "${spring.kafka.dlq.topic}", groupId = "dlq-group")
    public void consumeFromDlq(NotificationMessageDto notification) {
        log.warn("Обработка сообщения от DLQ: {}", notification);

    }
}
