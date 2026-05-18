package com.example.consumer.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.consumer.service.TelegramSenderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LevelUpListener {

    private final TelegramSenderService telegramSenderService;

    @KafkaListener(topics = "level-up-events", groupId = "notification-group")
    public void listenLevelUp(String message) {

        System.out.println("🔔 [NOTIFICATION SERVICE] MESSAGE INTERCEPTION: " + message);
        String telegramMessage = " <b> New Achievement in Elei </b> \n\n " + message;

        telegramSenderService.sendMessage(telegramMessage);
    }
}
