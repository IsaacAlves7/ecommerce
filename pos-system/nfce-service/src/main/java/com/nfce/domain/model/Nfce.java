package com.nfce.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Nfce {

    private Long id;
    private String accessKey;          // Chave de acesso 44 dígitos
    private String nfceNumber;         // Número da NF-e
    private String series;             // Série
    private NfceStatus status;
    private String saleCode;           // Referência à venda
    private Long saleId;

    // Emitente
    private String issuerCnpj;
    private String issuerName;
    private String issuerFantasyName;
    private String issuerAddress;

    // Destinatário
    private String customerDocument;
    private String customerName;
    private String customerEmail;

    // Totais
    private BigDecimal productTotal;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
    private BigDecimal total;

    // Pagamento
    private String paymentMethod;
    private BigDecimal paymentAmount;

    // Items
    private List<NfceItem> items;

    // Chaves fiscais
    private String qrCodeUrl;
    private String qrCodeBase64;
    private String xmlContent;
    private String protocol;
    private String danfeUrl;           // URL do PDF do DANFE

    // Webhook
    private String webhookUrl;
    private Boolean webhookSent;
    private LocalDateTime webhookSentAt;

    private LocalDateTime issuedAt;
    private LocalDateTime createdAt;

    public static Nfce create(String saleCode, Long saleId,
                               String customerDocument, String customerName, String customerEmail,
                               String paymentMethod, List<NfceItem> items,
                               BigDecimal total, BigDecimal taxTotal) {
        String accessKey = generateAccessKey();
        return Nfce.builder()
                .accessKey(accessKey)
                .nfceNumber(generateNumber())
                .series("001")
                .status(NfceStatus.PROCESSING)
                .saleCode(saleCode)
                .saleId(saleId)
                .issuerCnpj("00000000000000")   // configurar via properties em produção
                .issuerName("Empresa Demonstração Ltda")
                .issuerFantasyName("Loja POS")
                .issuerAddress("Rua Exemplo, 100 - São Paulo/SP")
                .customerDocument(customerDocument)
                .customerName(customerName)
                .customerEmail(customerEmail)
                .productTotal(total)
                .discountTotal(BigDecimal.ZERO)
                .taxTotal(taxTotal)
                .total(total)
                .paymentMethod(paymentMethod)
                .paymentAmount(total)
                .items(items)
                .webhookSent(false)
                .issuedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void authorize(String protocol, String xmlContent, String qrCodeUrl, String qrCodeBase64, String danfeUrl) {
        this.protocol = protocol;
        this.xmlContent = xmlContent;
        this.qrCodeUrl = qrCodeUrl;
        this.qrCodeBase64 = qrCodeBase64;
        this.danfeUrl = danfeUrl;
        this.status = NfceStatus.AUTHORIZED;
    }

    public void reject(String reason) {
        this.status = NfceStatus.REJECTED;
    }

    public void markWebhookSent() {
        this.webhookSent = true;
        this.webhookSentAt = LocalDateTime.now();
    }

    private static String generateAccessKey() {
        return UUID.randomUUID().toString().replace("-","").substring(0,44).toUpperCase();
    }

    private static String generateNumber() {
        return String.valueOf(System.currentTimeMillis()).substring(5);
    }

    public enum NfceStatus { PROCESSING, AUTHORIZED, REJECTED, CANCELLED }
}
