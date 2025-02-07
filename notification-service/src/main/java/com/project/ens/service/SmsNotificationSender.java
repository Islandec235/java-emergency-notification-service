package com.project.ens.service;

import com.project.ens.dto.NotificationMessageDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsNotificationSender {
    @Value("${twilio.account-sid}")
    private String accountSid;
    @Value("${twilio.account-token}")
    private String authToken;
    @Value("${twilio.phone-number}")
    private String twilioPhone;

    @Async
    @KafkaListener(topics = "sms-notification", groupId = "notification-group")
    public void send(NotificationMessageDto messageDto) {
        try {
            Twilio.init(accountSid, authToken);
            log.info("Отправка по SMS клиенту {}: {}", messageDto.getRecipient(), messageDto.getMessage());

            Message message = Message.creator(
                    new PhoneNumber(messageDto.getRecipient()),
                    new PhoneNumber(twilioPhone),
                    messageDto.getMessage()
            ).create();
            log.info("SMS успешно отправлено, SID: {}", message.getSid());
        } catch (e) {
            throw new ...
        }

    }
}
