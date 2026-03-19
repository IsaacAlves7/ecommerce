package com.pos.domain.ports.out;

import com.pos.domain.model.Sale;

public interface SaleEventPublisher {
    void publishSaleConfirmed(Sale sale);
    void publishSaleCancelled(Sale sale);
}
