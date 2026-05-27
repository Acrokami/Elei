package com.acrobtw.elei.consumer.service;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramSenderService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.admin.chat-id}")
    private String chatId;

    @Value("${telegram.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public void sendMessage(String text) {
        String url = apiUrl + "/bot" + botToken + "/sendMessage";

        Map<String, String> request = new HashMap<>();
        request.put("chat_id", chatId);
        request.put("text", text);


        try {
            restTemplate.postForObject(url, request, String.class);
            log.info("Telegram notification successfully delivered");
        } catch (RestClientException e) {
            log.error("Failed to send Telegram notification", e);
        }
    }
}
