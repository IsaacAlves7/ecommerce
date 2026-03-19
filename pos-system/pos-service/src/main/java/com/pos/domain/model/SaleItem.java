package com.pos.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class SaleItem {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private String productDescription;
    private String ncm;          // Nomenclatura Comum do Mercosul
    private String cfop;         // Código Fiscal de Operações
    private String unit;         // Unidade de medida
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal subtotal;
    private BigDecimal taxRate;  // Alíquota de imposto (%)
    private BigDecimal taxAmount;

    public static SaleItem create(Long productId, String productCode, String productName,
                                   String ncm, String cfop, String unit,
                                   Integer quantity, BigDecimal unitPrice, BigDecimal taxRate) {
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal taxAmount = subtotal.multiply(taxRate).divide(BigDecimal.valueOf(100));
        return SaleItem.builder()
                .productId(productId)
                .productCode(productCode)
                .productName(productName)
                .ncm(ncm)
                .cfop(cfop)
                .unit(unit)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .discount(BigDecimal.ZERO)
                .subtotal(subtotal)
                .taxRate(taxRate)
                .taxAmount(taxAmount)
                .build();
    }
}
