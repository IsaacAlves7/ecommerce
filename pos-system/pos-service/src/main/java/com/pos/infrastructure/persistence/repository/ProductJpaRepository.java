package com.pos.infrastructure.persistence.repository;

import com.pos.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
    Optional<ProductJpaEntity> findByBarcode(String barcode);
    Optional<ProductJpaEntity> findByCode(String code);

    @Query("SELECT p FROM ProductJpaEntity p WHERE p.status = 'ACTIVE' AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(p.code) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "p.barcode LIKE CONCAT('%',:q,'%'))")
    List<ProductJpaEntity> search(@Param("q") String query);
}
