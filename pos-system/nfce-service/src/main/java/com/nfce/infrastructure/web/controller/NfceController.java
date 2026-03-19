package com.nfce.infrastructure.web.controller;

import com.nfce.domain.model.Nfce;
import com.nfce.domain.ports.in.NfceUseCase;
import com.nfce.infrastructure.web.dto.response.NfceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nfce")
@RequiredArgsConstructor
@Tag(name = "NFC-e", description = "Nota Fiscal de Consumidor Eletrônica")
public class NfceController {

    private final NfceUseCase nfceUseCase;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar NFC-e por ID")
    public ResponseEntity<NfceResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(NfceResponse.from(nfceUseCase.findById(id)));
    }

    @GetMapping("/sale/{saleCode}")
    @Operation(summary = "Buscar NFC-e pela venda")
    public ResponseEntity<NfceResponse> findBySaleCode(@PathVariable String saleCode) {
        return ResponseEntity.ok(NfceResponse.from(nfceUseCase.findBySaleCode(saleCode)));
    }

    @GetMapping("/key/{accessKey}")
    @Operation(summary = "Consultar NFC-e pela chave de acesso")
    public ResponseEntity<NfceResponse> findByAccessKey(@PathVariable String accessKey) {
        return ResponseEntity.ok(NfceResponse.from(nfceUseCase.findByAccessKey(accessKey)));
    }

    @GetMapping("/{id}/danfe")
    @Operation(summary = "Baixar DANFE NFC-e (HTML)")
    public ResponseEntity<byte[]> getDanfe(@PathVariable Long id) {
        byte[] danfe = nfceUseCase.getDanfePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=danfe-nfce-" + id + ".html")
                .contentType(MediaType.TEXT_HTML)
                .body(danfe);
    }

    @GetMapping("/{id}/xml")
    @Operation(summary = "Baixar XML da NFC-e")
    public ResponseEntity<byte[]> getXml(@PathVariable Long id) {
        Nfce nfce = nfceUseCase.findById(id);
        byte[] xml = nfce.getXmlContent() != null ? nfce.getXmlContent().getBytes() : new byte[0];
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=nfce-" + nfce.getAccessKey() + ".xml")
                .contentType(MediaType.APPLICATION_XML)
                .body(xml);
    }
}
