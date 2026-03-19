package com.notification.infrastructure.messaging;

import com.notification.application.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NfceAuthorizedConsumer {

    private final EmailNotificationService emailService;

    @KafkaListener(
        topics = "nfce.authorized",
        groupId = "notification-service",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleNfceAuthorized(ConsumerRecord<String, Map<String, Object>> record) {
        log.info("📩 [notification] Mensagem recebida: key={} offset={}", record.key(), record.offset());
        try {
            Map<String, Object> event = record.value();
            String customerEmail = (String) event.get("customerEmail");

            if (customerEmail == null || customerEmail.isBlank()) {
                log.warn("⚠️ Email do cliente não informado para saleCode={}", event.get("saleCode"));
                return;
            }

            emailService.sendNfceEmail(event);
            log.info("✅ Email NFC-e enviado para: {}", customerEmail);
        } catch (Exception e) {
            log.error("❌ Erro ao processar nfce.authorized: {}", e.getMessage(), e);
        }
    }
}
