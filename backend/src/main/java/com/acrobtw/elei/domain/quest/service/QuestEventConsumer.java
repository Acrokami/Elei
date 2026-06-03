package com.acrobtw.elei.domain.quest.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.acrobtw.elei.domain.quest.dto.QuestEventPayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;


@Slf4j
@Service
@RequiredArgsConstructor
public class QuestEventConsumer {

    private final QuestEngineService questEngine;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.user-activities:user-activities}", groupId = "quest-engine-group")
    public void listenActivityEvent(String message) {
        try {
            QuestEventPayload event = objectMapper.readValue(message, QuestEventPayload.class);
            log.debug("Received quest event from Kafka: {} for user {}", event.eventType(), event.username());


            questEngine.processEvent(event.username(), event.eventType());

        } catch (Exception e) {
            log.error("Failed to process quest event message: {}", message, e);
            throw new RuntimeException("Error processing quest Kafka message", e);
        }
    }

    @KafkaListener(topics = "${kafka.topics.user-activities:user-activities}.DLT", groupId = "dlq-group")
    public void listenDlq(String message) {
       log.error("DLT quest message received, manual intervention required. Message: {}", message);
    }
}
