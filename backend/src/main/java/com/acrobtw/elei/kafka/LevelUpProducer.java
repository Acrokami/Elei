package com.acrobtw.elei.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class LevelUpProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topics.level-up}")
    private String levelUpTopic;

    public void sendLevelUpEvent(Long userId, Long newLevel) {
        String message = String.format("User %d reached level %d", userId, newLevel);

        kafkaTemplate.send(levelUpTopic, message)
        .whenComplete((result, ex) -> {
            if(ex != null) {
                log.error("Failed to send level-up event for userId={}, level={}", userId, newLevel, ex);
            } else {
                log.info("Level-up event sent: userId={}, level={}, offset={}",
                        userId, newLevel, result.getRecordMetadata().offset());
            }
        });
    }
}
