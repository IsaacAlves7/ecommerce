package com.pos.infrastructure.web.dto.response;

import com.pos.domain.model.Sale;
import com.pos.domain.model.SaleItem;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class SaleResponse {
    private Long id;
    private String saleCode;
    private String status;
    private List<ItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;
    private String paymentMethod;
    private String customerEmail;
    private String customerName;
    private String terminalId;
    private LocalDateTime createdAt;

    @Data @Builder
    public static class ItemResponse {
        private String productCode;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
        private BigDecimal taxAmount;
    }

    public static SaleResponse from(Sale s) {
        List<ItemResponse> items = s.getItems() == null ? List.of() :
            s.getItems().stream().map(i -> ItemResponse.builder()
                .productCode(i.getProductCode())
                .productName(i.getProductName())
                .quantity(i.getQuantity())
                .unitPrice(i.getUnitPrice())
                .subtotal(i.getSubtotal())
                .taxAmount(i.getTaxAmount())
                .build()).collect(Collectors.toList());

        return SaleResponse.builder()
                .id(s.getId())
                .saleCode(s.getSaleCode())
                .status(s.getStatus().name())
                .items(items)
                .subtotal(s.getSubtotal())
                .discount(s.getDiscount())
                .total(s.getTotal())
                .paymentMethod(s.getPaymentMethod().name())
                .customerEmail(s.getCustomerEmail())
                .customerName(s.getCustomerName())
                .terminalId(s.getTerminalId())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
