package com.pos.domain.ports.out;

import com.pos.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findByBarcode(String barcode);
    Optional<Product> findByCode(String code);
    List<Product> findAll();
    List<Product> search(String query);
}
