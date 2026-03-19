package com.pos.domain.ports.out;

import com.pos.domain.model.Sale;
import java.util.List;
import java.util.Optional;

public interface SaleRepository {
    Sale save(Sale sale);
    Optional<Sale> findById(Long id);
    Optional<Sale> findBySaleCode(String code);
    List<Sale> findAll(int page, int size);
}
