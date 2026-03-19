package com.nfce.domain.ports.out;

import com.nfce.domain.model.Nfce;
import java.util.Optional;

public interface NfceRepository {
    Nfce save(Nfce nfce);
    Optional<Nfce> findById(Long id);
    Optional<Nfce> findBySaleCode(String saleCode);
    Optional<Nfce> findByAccessKey(String accessKey);
}
