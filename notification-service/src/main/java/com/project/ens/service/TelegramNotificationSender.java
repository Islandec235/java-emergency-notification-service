package com.project.ens.service;

import com.project.ens.dto.NotificationMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramNotificationSender {
    @Value("${telegram.bot-token}")
    private String botToken;
    @Value("{telegram.chat-id}")
    private String defaultChatId;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    @KafkaListener(topics = "telegram-notification", groupId = "notification-group")
    public void send(NotificationMessageDto messageDto) {
        try {
            log.info("Отправка сообщением в Telegram клиенту {}: {}",
                    messageDto.getRecipient(), messageDto.getMessage());
            String chatId = messageDto.getRecipient();
            String text = messageDto.getMessage();

            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + text;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            log.info("Отправлено сообщение в Telegram клиенту {}: {}",
                    chatId, response.getBody());
        } catch (e) {
            throw new ...
        }

    }
}
