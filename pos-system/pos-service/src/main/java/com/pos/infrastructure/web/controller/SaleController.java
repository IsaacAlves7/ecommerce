package com.pos.infrastructure.web.controller;

import com.pos.domain.model.Sale;
import com.pos.domain.model.SaleItem;
import com.pos.domain.ports.in.SaleUseCase;
import com.pos.infrastructure.web.dto.request.CreateSaleRequest;
import com.pos.infrastructure.web.dto.response.SaleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Tag(name = "Vendas POS", description = "Gerenciamento de vendas no ponto de venda")
public class SaleController {

    private final SaleUseCase saleUseCase;

    @PostMapping
    @Operation(summary = "Criar nova venda")
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody CreateSaleRequest req) {
        List<SaleItem> items = req.getItems().stream().map(i ->
            SaleItem.create(i.getProductId(), i.getProductCode(), i.getProductName(),
                i.getNcm(), i.getCfop(), i.getUnit(),
                i.getQuantity(), i.getUnitPrice(), i.getTaxRate())
        ).collect(Collectors.toList());

        Sale sale = Sale.create(items, Sale.PaymentMethod.valueOf(req.getPaymentMethod()),
                req.getCustomerEmail(), req.getCustomerDocument(),
                req.getCustomerName(), req.getTerminalId(), req.getOperatorId());

        return ResponseEntity.status(HttpStatus.CREATED).body(SaleResponse.from(saleUseCase.createSale(sale)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venda por ID")
    public ResponseEntity<SaleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(SaleResponse.from(saleUseCase.findById(id)));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Buscar venda por código")
    public ResponseEntity<SaleResponse> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(SaleResponse.from(saleUseCase.findBySaleCode(code)));
    }

    @GetMapping
    @Operation(summary = "Listar vendas")
    public ResponseEntity<List<SaleResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<SaleResponse> sales = saleUseCase.findAll(page, size)
                .stream().map(SaleResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(sales);
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirmar venda — dispara geração de NFC-e via Kafka")
    public ResponseEntity<SaleResponse> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(SaleResponse.from(saleUseCase.confirmSale(id)));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancelar venda")
    public ResponseEntity<SaleResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(SaleResponse.from(saleUseCase.cancelSale(id)));
    }
}
