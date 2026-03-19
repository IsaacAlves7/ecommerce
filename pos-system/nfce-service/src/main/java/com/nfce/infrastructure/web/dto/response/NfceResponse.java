package com.nfce.infrastructure.web.dto.response;

import com.nfce.domain.model.Nfce;
import com.nfce.domain.model.NfceItem;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class NfceResponse {
    private Long id;
    private String accessKey;
    private String nfceNumber;
    private String series;
    private String status;
    private String saleCode;
    private String customerName;
    private String customerDocument;
    private String customerEmail;
    private BigDecimal total;
    private BigDecimal taxTotal;
    private String paymentMethod;
    private String qrCodeUrl;
    private String qrCodeBase64;
    private String protocol;
    private List<ItemLine> items;
    private LocalDateTime issuedAt;

    @Data @Builder
    public static class ItemLine {
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }

    public static NfceResponse from(Nfce n) {
        List<ItemLine> items = n.getItems() == null ? List.of() :
            n.getItems().stream().map(i -> ItemLine.builder()
                .productName(i.getProductName()).quantity(i.getQuantity())
                .unitPrice(i.getUnitPrice()).subtotal(i.getSubtotal()).build()
            ).collect(Collectors.toList());

        return NfceResponse.builder()
                .id(n.getId()).accessKey(n.getAccessKey()).nfceNumber(n.getNfceNumber())
                .series(n.getSeries()).status(n.getStatus().name()).saleCode(n.getSaleCode())
                .customerName(n.getCustomerName()).customerDocument(n.getCustomerDocument())
                .customerEmail(n.getCustomerEmail()).total(n.getTotal()).taxTotal(n.getTaxTotal())
                .paymentMethod(n.getPaymentMethod()).qrCodeUrl(n.getQrCodeUrl())
                .qrCodeBase64(n.getQrCodeBase64()).protocol(n.getProtocol())
                .items(items).issuedAt(n.getIssuedAt()).build();
    }
}
