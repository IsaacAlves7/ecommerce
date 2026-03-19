package com.pos.infrastructure.persistence.adapter;

import com.pos.domain.model.Sale;
import com.pos.domain.model.SaleItem;
import com.pos.domain.ports.out.SaleRepository;
import com.pos.infrastructure.persistence.entity.SaleItemJpaEntity;
import com.pos.infrastructure.persistence.entity.SaleJpaEntity;
import com.pos.infrastructure.persistence.repository.SaleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SaleRepositoryAdapter implements SaleRepository {

    private final SaleJpaRepository jpa;

    @Override
    public Sale save(Sale sale) {
        SaleJpaEntity entity = toEntity(sale);
        // set back-reference for items
        if (entity.getItems() != null)
            entity.getItems().forEach(i -> i.setSale(entity));
        return toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Sale> findBySaleCode(String code) {
        return jpa.findBySaleCode(code).map(this::toDomain);
    }

    @Override
    public List<Sale> findAll(int page, int size) {
        return jpa.findAllBy(PageRequest.of(page, size)).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    private SaleJpaEntity toEntity(Sale s) {
        SaleJpaEntity e = SaleJpaEntity.builder()
                .id(s.getId())
                .saleCode(s.getSaleCode())
                .status(SaleJpaEntity.SaleStatus.valueOf(s.getStatus().name()))
                .subtotal(s.getSubtotal())
                .discount(s.getDiscount())
                .total(s.getTotal())
                .paymentMethod(SaleJpaEntity.PaymentMethod.valueOf(s.getPaymentMethod().name()))
                .customerEmail(s.getCustomerEmail())
                .customerDocument(s.getCustomerDocument())
                .customerName(s.getCustomerName())
                .terminalId(s.getTerminalId())
                .operatorId(s.getOperatorId())
                .build();

        if (s.getItems() != null) {
            List<SaleItemJpaEntity> items = s.getItems().stream().map(item ->
                SaleItemJpaEntity.builder()
                    .id(item.getId())
                    .sale(e)
                    .productId(item.getProductId())
                    .productCode(item.getProductCode())
                    .productName(item.getProductName())
                    .ncm(item.getNcm())
                    .cfop(item.getCfop())
                    .unit(item.getUnit())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .discount(item.getDiscount())
                    .subtotal(item.getSubtotal())
                    .taxRate(item.getTaxRate())
                    .taxAmount(item.getTaxAmount())
                    .build()
            ).collect(Collectors.toList());
            e.setItems(items);
        }
        return e;
    }

    private Sale toDomain(SaleJpaEntity e) {
        List<SaleItem> items = e.getItems() == null ? List.of() :
            e.getItems().stream().map(i -> SaleItem.builder()
                    .id(i.getId())
                    .productId(i.getProductId())
                    .productCode(i.getProductCode())
                    .productName(i.getProductName())
                    .ncm(i.getNcm())
                    .cfop(i.getCfop())
                    .unit(i.getUnit())
                    .quantity(i.getQuantity())
                    .unitPrice(i.getUnitPrice())
                    .discount(i.getDiscount())
                    .subtotal(i.getSubtotal())
                    .taxRate(i.getTaxRate())
                    .taxAmount(i.getTaxAmount())
                    .build()).collect(Collectors.toList());

        return Sale.builder()
                .id(e.getId())
                .saleCode(e.getSaleCode())
                .status(Sale.SaleStatus.valueOf(e.getStatus().name()))
                .items(items)
                .subtotal(e.getSubtotal())
                .discount(e.getDiscount())
                .total(e.getTotal())
                .paymentMethod(Sale.PaymentMethod.valueOf(e.getPaymentMethod().name()))
                .customerEmail(e.getCustomerEmail())
                .customerDocument(e.getCustomerDocument())
                .customerName(e.getCustomerName())
                .terminalId(e.getTerminalId())
                .operatorId(e.getOperatorId())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
