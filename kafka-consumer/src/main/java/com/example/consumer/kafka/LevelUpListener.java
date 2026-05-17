package com.example.consumer.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LevelUpListener {

    @KafkaListener(topics = "level-up-events", groupId = "notification-group")
    public void listenLevelUp(String message) {

        System.out.println("🔔 [NOTIFICATION SERVICE]");
        System.out.println("📥 Text: " + message);


    }
}
