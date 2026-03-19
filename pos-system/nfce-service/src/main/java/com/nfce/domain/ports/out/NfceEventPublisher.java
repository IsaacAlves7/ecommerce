package com.nfce.domain.ports.out;

import com.nfce.domain.model.Nfce;

public interface NfceEventPublisher {
    void publishNfceAuthorized(Nfce nfce);
}
