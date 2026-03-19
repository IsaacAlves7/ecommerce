package com.pos.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class Product {
    private Long id;
    private String code;
    private String barcode;
    private String name;
    private String description;
    private String ncm;
    private String cfop;
    private String unit;
    private BigDecimal price;
    private BigDecimal taxRate;
    private Integer stockQuantity;
    private ProductStatus status;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum ProductStatus { ACTIVE, INACTIVE }
}
