package com.acrobtw.elei.domain.notification;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.domain.notification.dto.NotificationPayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;



@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final TelegramSenderService telegramSenderService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "level-up-events", groupId = "notification-group")
    public void listenNotification(String message) {
        log.info("Received level-up event: {}", message);

        try {
            NotificationPayload payload = objectMapper.readValue(message, NotificationPayload.class);
            String telegramMessage = String.format("<b>New Achievement in Elei</b>\n\nCitizen <b>%s</b> %s",
                payload.username(), payload.message());
            telegramSenderService.sendMessage(telegramMessage);

            messagingTemplate.convertAndSend("/topic/levelup", payload);
            log.info("[WEBSOCKET] Successfully pushed to /topic/levelup for user{}", payload);
        } catch (Exception e) {
            log.error("Failed to process level-up event", e);
            throw new RuntimeException("Error processing Kafka message", e);
        }
    }


    @KafkaListener(topics = "level-up-events.DLT", groupId = "dlq-group")
    public void listenDlq(String message) {
       log.error("DLT message received, manual intervention required. Message: {}", message);
    }
}
