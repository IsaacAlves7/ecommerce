package com.nfce.infrastructure.messaging;

import com.nfce.domain.ports.in.NfceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaleConfirmedConsumer {

    private final NfceUseCase nfceUseCase;

    @KafkaListener(
        topics = "pos.sale.confirmed",
        groupId = "nfce-service",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleSaleConfirmed(ConsumerRecord<String, Map<String, Object>> record) {
        log.info("📩 Mensagem recebida: topic={} key={} offset={}",
                record.topic(), record.key(), record.offset());
        try {
            Map<String, Object> saleEvent = record.value();
            nfceUseCase.generateFromSale(saleEvent);
        } catch (Exception e) {
            log.error("❌ Erro ao processar sale.confirmed: {}", e.getMessage(), e);
            // Dead Letter Queue seria configurada aqui em produção
        }
    }
}
