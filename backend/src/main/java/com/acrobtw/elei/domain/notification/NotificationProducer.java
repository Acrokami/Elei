package com.acrobtw.elei.domain.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.domain.notification.dto.NotificationPayload;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.level-up}")
    private String levelUpTopic;

    public void sendLevelUpEvent(String username, Long newLevel) {
        try {
            NotificationPayload payload = new NotificationPayload(
                username,
                "reached level " + newLevel + "!",
                "LEVEL_UP"
            );

            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(levelUpTopic, message)
            .whenComplete((result, ex) -> {
                if(ex != null) {
                    log.error("Failed to send level-up event for user={}, level={}", username, newLevel, ex);
                } else {
                    log.info("Level-up event sent: user={}, level={}, offset={}", username, newLevel, result.getRecordMetadata().offset());
                }
            });
        } catch (Exception e) {
            log.error("Failed to serialize level-up event for user={}", username, e);
        }
    }
}
