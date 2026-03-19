package com.pos.domain.ports.in;

import com.pos.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductUseCase {
    Product create(Product product);
    Product findById(Long id);
    Optional<Product> findByBarcode(String barcode);
    Optional<Product> findByCode(String code);
    List<Product> findAll();
    List<Product> search(String query);
    Product update(Long id, Product product);
    void deactivate(Long id);
}
