package com.pos.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Sale {

    private Long id;
    private String saleCode;
    private SaleStatus status;
    private List<SaleItem> items;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;
    private PaymentMethod paymentMethod;
    private String customerEmail;
    private String customerDocument; // CPF/CNPJ
    private String customerName;
    private String terminalId;
    private String operatorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Sale create(List<SaleItem> items, PaymentMethod paymentMethod,
                               String customerEmail, String customerDocument,
                               String customerName, String terminalId, String operatorId) {
        BigDecimal subtotal = items.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Sale.builder()
                .saleCode(generateCode())
                .status(SaleStatus.PENDING)
                .items(items)
                .subtotal(subtotal)
                .discount(BigDecimal.ZERO)
                .total(subtotal)
                .paymentMethod(paymentMethod)
                .customerEmail(customerEmail)
                .customerDocument(customerDocument)
                .customerName(customerName)
                .terminalId(terminalId)
                .operatorId(operatorId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void applyDiscount(BigDecimal discountAmount) {
        if (discountAmount.compareTo(subtotal) > 0)
            throw new IllegalArgumentException("Desconto não pode ser maior que o subtotal.");
        this.discount = discountAmount;
        this.total = subtotal.subtract(discountAmount);
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        if (this.status != SaleStatus.PENDING)
            throw new IllegalStateException("Venda já foi processada.");
        this.status = SaleStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == SaleStatus.CANCELLED)
            throw new IllegalStateException("Venda já está cancelada.");
        this.status = SaleStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    private static String generateCode() {
        return "VND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public enum SaleStatus { PENDING, CONFIRMED, CANCELLED, REFUNDED }
    public enum PaymentMethod { CASH, CREDIT_CARD, DEBIT_CARD, PIX, VOUCHER }
}
