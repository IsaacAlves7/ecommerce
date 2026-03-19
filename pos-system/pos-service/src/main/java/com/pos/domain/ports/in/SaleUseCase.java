package com.pos.domain.ports.in;

import com.pos.domain.model.Sale;
import java.util.List;

public interface SaleUseCase {
    Sale createSale(Sale sale);
    Sale findById(Long id);
    Sale findBySaleCode(String code);
    List<Sale> findAll(int page, int size);
    Sale confirmSale(Long id);
    Sale cancelSale(Long id);
}
