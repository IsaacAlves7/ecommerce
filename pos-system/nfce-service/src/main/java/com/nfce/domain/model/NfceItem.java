package com.nfce.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class NfceItem {
    private Integer itemNumber;
    private String productCode;
    private String productName;
    private String ncm;
    private String cfop;
    private String unit;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
}
