package com.project.ens.service;

import com.project.ens.dto.NotificationMessageDto;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, NotificationMessageDto> kafkaTemplate;
    private final MeterRegistry meterRegistry;

    @Value("${spring.kafka.dlq.topic}")
    private String dlqTopic;

    @Timed(value = "dlq.notification.send")
    public void sendToDlq(NotificationMessageDto messageDto) {
        try {
            kafkaTemplate.send(dlqTopic, messageDto);
            meterRegistry.counter("dlq.notification.success").increment();
            log.info("Уведомление отправлено в DLQ: {}", messageDto);
        } catch (Exception e) {
            meterRegistry.counter("dlq.notification.failure").increment();
            throw e;
        }
    }
}
