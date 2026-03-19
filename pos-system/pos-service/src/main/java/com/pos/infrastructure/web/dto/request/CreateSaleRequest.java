package com.pos.infrastructure.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateSaleRequest {
    @NotNull @Valid
    private List<SaleItemRequest> items;

    @NotBlank
    private String paymentMethod;

    @Email
    private String customerEmail;
    private String customerDocument;
    private String customerName;

    @NotBlank
    private String terminalId;
    @NotBlank
    private String operatorId;

    @Data
    public static class SaleItemRequest {
        private Long productId;
        @NotBlank private String productCode;
        @NotBlank private String productName;
        private String ncm;
        private String cfop;
        private String unit = "UN";
        @NotNull @Min(1) private Integer quantity;
        @NotNull @DecimalMin("0.01") private BigDecimal unitPrice;
        private BigDecimal taxRate = BigDecimal.ZERO;
    }
}
