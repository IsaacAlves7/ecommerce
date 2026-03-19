package com.pos.application.service;

import com.pos.domain.exception.ResourceNotFoundException;
import com.pos.domain.model.Sale;
import com.pos.domain.ports.in.SaleUseCase;
import com.pos.domain.ports.out.SaleEventPublisher;
import com.pos.domain.ports.out.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleService implements SaleUseCase {

    private final SaleRepository saleRepository;
    private final SaleEventPublisher eventPublisher;

    @Override
    @Transactional
    public Sale createSale(Sale sale) {
        log.info("Criando venda: terminal={}", sale.getTerminalId());
        Sale saved = saleRepository.save(sale);
        log.info("Venda criada: code={}", saved.getSaleCode());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Sale findById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Sale findBySaleCode(String code) {
        return saleRepository.findBySaleCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> findAll(int page, int size) {
        return saleRepository.findAll(page, size);
    }

    @Override
    @Transactional
    public Sale confirmSale(Long id) {
        log.info("Confirmando venda: {}", id);
        Sale sale = findById(id);
        sale.confirm();
        Sale saved = saleRepository.save(sale);

        // Publica evento para Kafka → NFC-e Service irá consumir
        eventPublisher.publishSaleConfirmed(saved);
        log.info("Evento sale.confirmed publicado para venda: {}", saved.getSaleCode());
        return saved;
    }

    @Override
    @Transactional
    public Sale cancelSale(Long id) {
        log.info("Cancelando venda: {}", id);
        Sale sale = findById(id);
        sale.cancel();
        Sale saved = saleRepository.save(sale);
        eventPublisher.publishSaleCancelled(saved);
        return saved;
    }
}
