package com.noteplanning.discord.modules.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AbacatePayWebhookDTO {
    private Info data;
    private boolean devMode;
    private String event;

    @Data
    public static class Info {
        private Billing billing;
        private Payment payment;
    }

    @Data
    public static class Billing {
        private Customer customer;
        private List<Product> products;
        private Double amount;
        private String status;
    }

    @Data
    public static class Customer {
        private Metadata metadata;
    }

    @Data
    public static class Metadata {
        private String name;
        private String email;
        private String cellphone;
        private String taxId;
    }

    @Data
    public static class Product {
        private String externalId;
        private int quantity;
    }

    @Data
    public static class Payment {
        private Double amount;
        private String method;
        private Double fee;
    }
}