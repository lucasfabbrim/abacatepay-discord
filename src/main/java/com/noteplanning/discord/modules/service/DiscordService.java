package com.noteplanning.discord.modules.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noteplanning.discord.modules.dto.AbacatePayWebhookDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class DiscordService {

    @Value("${webhook.discord.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendToDiscord(AbacatePayWebhookDTO payload) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String discordMessage = formatMessage(payload);
        HttpEntity<String> request = new HttpEntity<>(discordMessage, headers);

        restTemplate.postForObject(webhookUrl, request, String.class);
    }

    private String formatMessage(AbacatePayWebhookDTO payload) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new DiscordMessage(
                "**VENDA REALIZADA** 🎉\n\n" +
                        "**Preço**: R$ " + payload.getData().getPayment().getAmount() + "\n\n" +
                        "**Dados do Comprador**: \n" +
                        "**Nome**: " + payload.getData().getBilling().getCustomer().getMetadata().getName() + "\n" +
                        "**E-mail**: " + payload.getData().getBilling().getCustomer().getMetadata().getEmail() + "\n\n" +
                        "**Data da Compra**: " + formatDate() + "\n\n"
        ));
    }
    private String formatDate() {
        DateTimeFormatter logFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        return LocalDateTime.now().format(logFormatter);
    }

    private record DiscordMessage(String content) {}
}