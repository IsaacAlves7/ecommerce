package com.nfce.infrastructure.persistence.adapter;

import com.nfce.domain.model.Nfce;
import com.nfce.domain.model.NfceItem;
import com.nfce.domain.ports.out.NfceRepository;
import com.nfce.infrastructure.persistence.entity.NfceItemJpaEntity;
import com.nfce.infrastructure.persistence.entity.NfceJpaEntity;
import com.nfce.infrastructure.persistence.repository.NfceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NfceRepositoryAdapter implements NfceRepository {

    private final NfceJpaRepository jpa;

    @Override
    public Nfce save(Nfce n) {
        NfceJpaEntity entity = toEntity(n);
        if (entity.getItems() != null) entity.getItems().forEach(i -> i.setNfce(entity));
        return toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Nfce> findById(Long id) { return jpa.findById(id).map(this::toDomain); }

    @Override
    public Optional<Nfce> findBySaleCode(String code) { return jpa.findBySaleCode(code).map(this::toDomain); }

    @Override
    public Optional<Nfce> findByAccessKey(String key) { return jpa.findByAccessKey(key).map(this::toDomain); }

    private NfceJpaEntity toEntity(Nfce n) {
        NfceJpaEntity e = NfceJpaEntity.builder()
                .id(n.getId()).accessKey(n.getAccessKey()).nfceNumber(n.getNfceNumber())
                .series(n.getSeries()).status(NfceJpaEntity.NfceStatus.valueOf(n.getStatus().name()))
                .saleCode(n.getSaleCode()).saleId(n.getSaleId())
                .issuerCnpj(n.getIssuerCnpj()).issuerName(n.getIssuerName())
                .customerDocument(n.getCustomerDocument()).customerName(n.getCustomerName())
                .customerEmail(n.getCustomerEmail())
                .productTotal(n.getProductTotal()).discountTotal(n.getDiscountTotal())
                .taxTotal(n.getTaxTotal()).total(n.getTotal())
                .paymentMethod(n.getPaymentMethod()).paymentAmount(n.getPaymentAmount())
                .qrCodeUrl(n.getQrCodeUrl()).qrCodeBase64(n.getQrCodeBase64())
                .xmlContent(n.getXmlContent()).protocol(n.getProtocol()).danfeUrl(n.getDanfeUrl())
                .webhookSent(n.getWebhookSent()).webhookSentAt(n.getWebhookSentAt())
                .issuedAt(n.getIssuedAt()).build();

        if (n.getItems() != null) {
            e.setItems(n.getItems().stream().map(i -> NfceItemJpaEntity.builder()
                    .nfce(e).itemNumber(i.getItemNumber()).productCode(i.getProductCode())
                    .productName(i.getProductName()).ncm(i.getNcm()).cfop(i.getCfop()).unit(i.getUnit())
                    .quantity(i.getQuantity()).unitPrice(i.getUnitPrice()).subtotal(i.getSubtotal())
                    .taxRate(i.getTaxRate()).taxAmount(i.getTaxAmount()).build()
            ).collect(Collectors.toList()));
        }
        return e;
    }

    private Nfce toDomain(NfceJpaEntity e) {
        List<NfceItem> items = e.getItems() == null ? List.of() :
            e.getItems().stream().map(i -> NfceItem.builder()
                    .itemNumber(i.getItemNumber()).productCode(i.getProductCode())
                    .productName(i.getProductName()).ncm(i.getNcm()).cfop(i.getCfop()).unit(i.getUnit())
                    .quantity(i.getQuantity()).unitPrice(i.getUnitPrice()).subtotal(i.getSubtotal())
                    .taxRate(i.getTaxRate()).taxAmount(i.getTaxAmount()).build()
            ).collect(Collectors.toList());

        return Nfce.builder()
                .id(e.getId()).accessKey(e.getAccessKey()).nfceNumber(e.getNfceNumber())
                .series(e.getSeries()).status(Nfce.NfceStatus.valueOf(e.getStatus().name()))
                .saleCode(e.getSaleCode()).saleId(e.getSaleId())
                .issuerCnpj(e.getIssuerCnpj()).issuerName(e.getIssuerName())
                .customerDocument(e.getCustomerDocument()).customerName(e.getCustomerName())
                .customerEmail(e.getCustomerEmail())
                .productTotal(e.getProductTotal()).discountTotal(e.getDiscountTotal())
                .taxTotal(e.getTaxTotal()).total(e.getTotal())
                .paymentMethod(e.getPaymentMethod()).paymentAmount(e.getPaymentAmount())
                .qrCodeUrl(e.getQrCodeUrl()).qrCodeBase64(e.getQrCodeBase64())
                .xmlContent(e.getXmlContent()).protocol(e.getProtocol()).danfeUrl(e.getDanfeUrl())
                .webhookSent(e.getWebhookSent()).webhookSentAt(e.getWebhookSentAt())
                .issuedAt(e.getIssuedAt()).createdAt(e.getCreatedAt()).items(items).build();
    }
}
