package com.acrobtw.elei.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LevelUpProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "level-up-events";

    public void sendLevelUpEvent(Long userId, Long newLevel) {
        String message = String.format("User %d reached level %d!", userId, newLevel);
        kafkaTemplate.send(TOPIC, message);
        System.out.println("[KAFKA PRODUCER] Message sent: " + message);
    }
}
