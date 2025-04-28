package com.noteplanning.discord.modules.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DiscordService {

    @Value("${webhook.discord.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendToDiscord(String message) {
        restTemplate.postForObject(webhookUrl, message, String.class);
    }

}
