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
                "🎉 **PAGAMENTO RECEBIDO** 🎉\n" +
                        "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                        "💰 **Valor:** R$ " + payload.getData().getPayment().getAmount() + "\n" +
                        "📋 **Método:** " + payload.getData().getPayment().getMethod() + "\n" +
                        "📦 **Produto:** " + payload.getData().getBilling().getProducts().get(0).getExternalId() + "\n" +
                        "👤 **Cliente:** " + payload.getData().getBilling().getCustomer().getMetadata().getName() + "\n" +
                        "📧 **Email:** " + payload.getData().getBilling().getCustomer().getMetadata().getEmail() + "\n" +
                        (payload.isDevMode() ? "🚧 **MODO DE DESENVOLVIMENTO**" : "")
        ));
    }

    private record DiscordMessage(String content) {}
}