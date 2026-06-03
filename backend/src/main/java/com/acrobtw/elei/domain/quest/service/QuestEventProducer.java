package com.acrobtw.elei.domain.quest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.domain.quest.enums.EventType;
import com.acrobtw.elei.domain.quest.dto.QuestEventPayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.user-activities:user-activities}")
    private String activitiesTopic;

    public void sendEvent(String username, EventType eventType) {
        try {
            QuestEventPayload payload = new QuestEventPayload(username, eventType);
            String message = objectMapper.writeValueAsString(payload);

            kafkaTemplate.send(activitiesTopic, message)
            .whenComplete((result, ex) -> {
                if(ex != null) {
                    log.error("Failed to send quest event: user={},type={}", username, eventType);
                } else {
                    log.info("Quest event sent: user={}, type={}", username, eventType);
                }
            });
        } catch (Exception e) {
            log.error("Serialization error for quest event", e);
        }
    }
}
