package com.pos.infrastructure.persistence.repository;

import com.pos.infrastructure.persistence.entity.SaleJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleJpaRepository extends JpaRepository<SaleJpaEntity, Long> {
    Optional<SaleJpaEntity> findBySaleCode(String saleCode);
    List<SaleJpaEntity> findAllBy(Pageable pageable);
}
