package com.project.ens.service;

import com.project.ens.dto.NotificationMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationSender {
    private final JavaMailSender mailSender;

    @Async
    @KafkaListener(topics = "email-notification", groupId = "notification-group")
    public void send(NotificationMessageDto messageDto) {
        try {
            log.info("Отправка по email клиенту {}: {}", messageDto.getRecipient(), messageDto.getMessage());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(messageDto.getRecipient());
            mailMessage.setText(messageDto.getMessage());

            mailSender.send(mailMessage);
        } catch (e) {
            throw new ...
        }
    }
}
