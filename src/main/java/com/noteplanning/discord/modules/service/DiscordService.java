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
                "**SALES - COMPLETED** 🎉\n\n" +
                        "Price: R$ " + formatPrice(payload.getData().getPayment().getAmount()) + "\n" +
                        "Payment: " + payload.getData().getPayment().getMethod() + "\n" +
                        "Product: " + payload.getData().getBilling().getProducts().get(0).getExternalId() + "\n" +
                        "Name of Costumer: " + payload.getData().getBilling().getCustomer().getMetadata().getName() + "\n" +
                        "E-mail of Costumer: " + payload.getData().getBilling().getCustomer().getMetadata().getEmail() + "\n" +
                        "Date: " + formatDate() + "\n\n" +
                        (payload.isDevMode() ? "Mode: Development" : "Mode: Production")
        ));
    }
    private String formatDate() {
        DateTimeFormatter logFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(logFormatter);
    }
    private String formatPrice(Double price){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatter.format(price);
    }

    private record DiscordMessage(String content) {}
}