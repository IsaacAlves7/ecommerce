package com.nfce.infrastructure.messaging;

import com.nfce.domain.model.Nfce;
import com.nfce.domain.ports.out.NfceEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNfceEventPublisher implements NfceEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String TOPIC_NFCE_AUTHORIZED = "nfce.authorized";

    @Override
    public void publishNfceAuthorized(Nfce nfce) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "NFCE_AUTHORIZED");
            event.put("eventTime", LocalDateTime.now().toString());
            event.put("nfceId", nfce.getId());
            event.put("accessKey", nfce.getAccessKey());
            event.put("nfceNumber", nfce.getNfceNumber());
            event.put("saleCode", nfce.getSaleCode());
            event.put("customerEmail", nfce.getCustomerEmail());
            event.put("customerName", nfce.getCustomerName());
            event.put("customerDocument", nfce.getCustomerDocument());
            event.put("issuerName", nfce.getIssuerName());
            event.put("issuerAddress", nfce.getIssuerAddress());
            event.put("total", nfce.getTotal());
            event.put("paymentMethod", nfce.getPaymentMethod());
            event.put("qrCodeBase64", nfce.getQrCodeBase64());
            event.put("qrCodeUrl", nfce.getQrCodeUrl());
            event.put("danfeBase64", nfce.getDanfeUrl()); // danfeUrl stores base64 in this impl
            event.put("issuedAt", nfce.getIssuedAt() != null ? nfce.getIssuedAt().toString() : null);
            event.put("items", nfce.getItems().stream().map(i -> {
                Map<String, Object> item = new HashMap<>();
                item.put("productName", i.getProductName());
                item.put("quantity", i.getQuantity());
                item.put("unitPrice", i.getUnitPrice());
                item.put("subtotal", i.getSubtotal());
                return item;
            }).collect(Collectors.toList()));

            kafkaTemplate.send(TOPIC_NFCE_AUTHORIZED, nfce.getSaleCode(), event);
            log.info("✅ Evento NFCE_AUTHORIZED publicado para: {}", nfce.getCustomerEmail());
        } catch (Exception e) {
            log.error("❌ Erro ao publicar NFCE_AUTHORIZED: {}", e.getMessage(), e);
        }
    }
}
