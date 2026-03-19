package com.nfce.domain.ports.in;

import com.nfce.domain.model.Nfce;

public interface NfceUseCase {
    Nfce generateFromSale(java.util.Map<String, Object> saleEvent);
    Nfce findById(Long id);
    Nfce findBySaleCode(String saleCode);
    Nfce findByAccessKey(String accessKey);
    byte[] getDanfePdf(Long id);
}
