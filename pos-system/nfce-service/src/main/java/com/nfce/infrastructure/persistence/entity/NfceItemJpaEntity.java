package com.nfce.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nfce_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NfceItemJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nfce_id", nullable = false)
    private NfceJpaEntity nfce;

    @Column(name = "item_number")
    private Integer itemNumber;

    @Column(name = "product_code", length = 50)
    private String productCode;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(length = 10) private String ncm;
    @Column(length = 10) private String cfop;
    @Column(length = 10) private String unit;
    @Column(nullable = false) private Integer quantity;

    @Column(name = "unit_price", precision = 15, scale = 2) private BigDecimal unitPrice;
    @Column(precision = 15, scale = 2) private BigDecimal subtotal;
    @Column(name = "tax_rate", precision = 5, scale = 2) private BigDecimal taxRate;
    @Column(name = "tax_amount", precision = 15, scale = 2) private BigDecimal taxAmount;
}
