package com.pos.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.domain.model.Sale;
import com.pos.domain.ports.out.SaleEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaSaleEventPublisher implements SaleEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public static final String TOPIC_SALE_CONFIRMED = "pos.sale.confirmed";
    public static final String TOPIC_SALE_CANCELLED = "pos.sale.cancelled";

    @Override
    public void publishSaleConfirmed(Sale sale) {
        try {
            Map<String, Object> event = buildEvent("SALE_CONFIRMED", sale);
            kafkaTemplate.send(TOPIC_SALE_CONFIRMED, sale.getSaleCode(), event);
            log.info("✅ Evento SALE_CONFIRMED publicado: topic={} key={}", TOPIC_SALE_CONFIRMED, sale.getSaleCode());
        } catch (Exception e) {
            log.error("❌ Erro ao publicar SALE_CONFIRMED: {}", e.getMessage(), e);
        }
    }

    @Override
    public void publishSaleCancelled(Sale sale) {
        try {
            Map<String, Object> event = buildEvent("SALE_CANCELLED", sale);
            kafkaTemplate.send(TOPIC_SALE_CANCELLED, sale.getSaleCode(), event);
            log.info("✅ Evento SALE_CANCELLED publicado: topic={}", TOPIC_SALE_CANCELLED);
        } catch (Exception e) {
            log.error("❌ Erro ao publicar SALE_CANCELLED: {}", e.getMessage(), e);
        }
    }

    private Map<String, Object> buildEvent(String eventType, Sale sale) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("eventTime", LocalDateTime.now().toString());
        event.put("saleId", sale.getId());
        event.put("saleCode", sale.getSaleCode());
        event.put("status", sale.getStatus().name());
        event.put("total", sale.getTotal());
        event.put("paymentMethod", sale.getPaymentMethod().name());
        event.put("customerEmail", sale.getCustomerEmail());
        event.put("customerDocument", sale.getCustomerDocument());
        event.put("customerName", sale.getCustomerName());
        event.put("terminalId", sale.getTerminalId());
        event.put("items", sale.getItems().stream().map(i -> {
            Map<String, Object> item = new HashMap<>();
            item.put("productCode", i.getProductCode());
            item.put("productName", i.getProductName());
            item.put("ncm", i.getNcm());
            item.put("cfop", i.getCfop());
            item.put("unit", i.getUnit());
            item.put("quantity", i.getQuantity());
            item.put("unitPrice", i.getUnitPrice());
            item.put("subtotal", i.getSubtotal());
            item.put("taxRate", i.getTaxRate());
            item.put("taxAmount", i.getTaxAmount());
            return item;
        }).toList());
        return event;
    }
}
