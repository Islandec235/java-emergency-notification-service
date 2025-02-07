package com.project.ens.service.impl;

import com.project.ens.dto.NotificationMessageDto;
import com.project.ens.handler.UnknownSenderException;
import com.project.ens.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final KafkaProducerService kafkaProducerService;
    private final EmailNotificationSender emailSender;
    private final SmsNotificationSender smsSender;
    private final TelegramNotificationSender telegramSender;

    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final long RETRY_DELAY_MS = 2000;

    @Override
    public void processNotification(NotificationMessageDto messageDto) {
        log.info("Обработка уведомления для пользователя: {}", messageDto.getRecipient());

        try {
            switch (messageDto.getType()) {
                case EMAIL -> emailSender.send(messageDto);
                case SMS -> smsSender.send(messageDto);
                case TELEGRAM -> telegramSender.send(messageDto);
                default -> throw new UnknownSenderException("Неизвестный тип отправки: " + messageDto.getType());
            }
        } catch (Exception e) {
            log.error("Ошибка обработки уведомления: {}. Повтор...", messageDto, e);
            retryNotification(messageDto, 1);
        }
    }

    private void retryNotification(NotificationMessageDto messageDto, int attempt) {
        if (attempt > MAX_RETRY_ATTEMPTS) {
            sendToDeadLetterQueue(messageDto);
            throw new ...
        }

        try {
            Thread.sleep(RETRY_DELAY_MS);
            log.info("Повтор вызова уведомления (попытка {}): {}", attempt, messageDto);
            processNotification(messageDto);
        } catch (InterruptedException e) {
            log.error("Повтор прерванной попытки получения уведомления: {}", messageDto, e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Повторная попытка для получения уведомления не удалась: {}. Попытка: {}", messageDto, attempt);
            retryNotification(messageDto, attempt + 1);
        }
    }

    private void sendToDeadLetterQueue(NotificationMessageDto messageDto) {
        kafkaProducerService.sendToDlq(messageDto);
    }
}
