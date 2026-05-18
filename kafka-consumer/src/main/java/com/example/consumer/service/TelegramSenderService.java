package com.example.consumer.service;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramSenderService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.admin.chat-id}")
    private String chatId;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMessage(String text) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, String> request = new HashMap<>();
        request.put("chat_id", chatId);
        request.put("text", text);


        try {
            restTemplate.postForObject(url, request, String.class);
            System.out.println("[TELEGRAM] Notification successfully delivered");
        } catch (Exception e) {
            System.err.println("[TELEGRAM] Delivery error: " + e.getMessage());
        }
    }
}
