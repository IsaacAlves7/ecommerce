package com.nfce.infrastructure.messaging;

import com.nfce.domain.model.Nfce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Webhook Service — envia NFC-e autorizada para sistemas externos via HTTP POST.
 * Configura a URL destino via variável de ambiente WEBHOOK_URL.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookDispatcherService {

    private final RestTemplate restTemplate;

    @Value("${webhook.nfce.url:#{null}}")
    private String webhookUrl;

    @Value("${webhook.nfce.secret:}")
    private String webhookSecret;

    public void dispatch(Nfce nfce) {
        if (webhookUrl == null || webhookUrl.isBlank()) {
            log.debug("Webhook não configurado, pulando dispatch.");
            return;
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("event", "NFCE_AUTHORIZED");
            payload.put("accessKey", nfce.getAccessKey());
            payload.put("nfceNumber", nfce.getNfceNumber());
            payload.put("saleCode", nfce.getSaleCode());
            payload.put("customerEmail", nfce.getCustomerEmail());
            payload.put("total", nfce.getTotal());
            payload.put("issuedAt", nfce.getIssuedAt());
            payload.put("qrCodeUrl", nfce.getQrCodeUrl());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (!webhookSecret.isBlank()) {
                headers.set("X-Webhook-Secret", webhookSecret);
            }

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            restTemplate.postForEntity(webhookUrl, request, Void.class);

            log.info("✅ Webhook NFC-e despachado para: {}", webhookUrl);
        } catch (Exception e) {
            log.error("❌ Falha ao enviar webhook: {} — {}", webhookUrl, e.getMessage());
        }
    }
}
