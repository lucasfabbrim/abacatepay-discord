package com.noteplanning.discord.modules.controller;

import com.noteplanning.discord.modules.service.DiscordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/abacatepay")
public class WebhookAbacatePayController {

    private final String expectedSecret;
    private final DiscordService discordService;

    public WebhookAbacatePayController(@Value("${abacatepay.token.secret}")
                                       String expectedSecret,
                                       DiscordService discordService
    )
    {
        this.expectedSecret = expectedSecret;
        this.discordService = discordService;
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestParam(name = "webhookSecret") String receivedSecret,
            @RequestBody String payload) {

        if (!expectedSecret.equals(receivedSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Secret inválido");
        }

        try {
            // Log para debug
            System.out.println("Payload recebido: " + payload);

            // Processe o payload conforme necessário
            discordService.sendToDiscord(payload);

            return ResponseEntity.ok("Webhook processado com sucesso");
        } catch (Exception e) {
            e.printStackTrace(); // Adicione logging adequado
            return ResponseEntity.internalServerError()
                    .body("Erro no processamento: " + e.getMessage());
        }
    }
}
