package com.pos.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SaleJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_code", unique = true, nullable = false, length = 20)
    private String saleCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus status;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal discount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    @Column(name = "customer_document", length = 20)
    private String customerDocument;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "terminal_id", length = 50)
    private String terminalId;

    @Column(name = "operator_id", length = 50)
    private String operatorId;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SaleItemJpaEntity> items;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum SaleStatus { PENDING, CONFIRMED, CANCELLED, REFUNDED }
    public enum PaymentMethod { CASH, CREDIT_CARD, DEBIT_CARD, PIX, VOUCHER }
}
