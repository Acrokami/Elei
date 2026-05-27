package com.acrobtw.elei.consumer.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.consumer.service.TelegramSenderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class LevelUpConsumer {

    private final TelegramSenderService telegramSenderService;

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "level-up-events", groupId = "notification-group")
    public void listenLevelUp(String message) {
        log.info("Received level-up event: {}", message);

        try {
            String telegramMessage = "<b>New Achievement in Elei</b>\n\n" + message;
            telegramSenderService.sendMessage(telegramMessage);
            messagingTemplate.convertAndSend("/topic/levelup", message);
            log.info("[WEBSOCKET] Successfully pushed to /topic/levelup");
        } catch (Exception e) {
            log.error("Failed to process level-up event", e);
            throw e;
        }
    }


    @KafkaListener(topics = "level-up-events.DLT", groupId = "dlq-group")
    public void listenDlq(String message) {
       log.error("DLT message received, manual intervention required. Message: {}", message);
    }
}
