package com.nfce.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nfces")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NfceJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_key", unique = true, nullable = false, length = 60)
    private String accessKey;

    @Column(name = "nfce_number", length = 20)
    private String nfceNumber;

    @Column(length = 10)
    private String series;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NfceStatus status;

    @Column(name = "sale_code", length = 30)
    private String saleCode;

    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "issuer_cnpj", length = 20)
    private String issuerCnpj;

    @Column(name = "issuer_name", length = 200)
    private String issuerName;

    @Column(name = "customer_document", length = 20)
    private String customerDocument;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    @Column(name = "product_total", precision = 15, scale = 2)
    private BigDecimal productTotal;

    @Column(name = "discount_total", precision = 15, scale = 2)
    private BigDecimal discountTotal;

    @Column(name = "tax_total", precision = 15, scale = 2)
    private BigDecimal taxTotal;

    @Column(precision = 15, scale = 2)
    private BigDecimal total;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(name = "payment_amount", precision = 15, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "qr_code_url", length = 500)
    private String qrCodeUrl;

    @Column(name = "qr_code_base64", columnDefinition = "TEXT")
    private String qrCodeBase64;

    @Column(name = "xml_content", columnDefinition = "TEXT")
    private String xmlContent;

    @Column(length = 50)
    private String protocol;

    @Column(name = "danfe_url", columnDefinition = "TEXT")
    private String danfeUrl;

    @Column(name = "webhook_sent")
    private Boolean webhookSent;

    @Column(name = "webhook_sent_at")
    private LocalDateTime webhookSentAt;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "nfce", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<NfceItemJpaEntity> items;

    public enum NfceStatus { PROCESSING, AUTHORIZED, REJECTED, CANCELLED }
}
