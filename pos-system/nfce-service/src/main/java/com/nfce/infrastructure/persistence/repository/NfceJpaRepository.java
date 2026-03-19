package com.nfce.infrastructure.persistence.repository;

import com.nfce.infrastructure.persistence.entity.NfceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NfceJpaRepository extends JpaRepository<NfceJpaEntity, Long> {
    Optional<NfceJpaEntity> findBySaleCode(String saleCode);
    Optional<NfceJpaEntity> findByAccessKey(String accessKey);
}
