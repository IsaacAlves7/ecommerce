package com.nfce.application.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nfce.domain.exception.NfceNotFoundException;
import com.nfce.domain.model.Nfce;
import com.nfce.domain.model.NfceItem;
import com.nfce.domain.ports.in.NfceUseCase;
import com.nfce.domain.ports.out.NfceEventPublisher;
import com.nfce.domain.ports.out.NfceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NfceService implements NfceUseCase {

    private final NfceRepository nfceRepository;
    private final NfceEventPublisher eventPublisher;
    private final DanfeGeneratorService danfeGenerator;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Nfce generateFromSale(Map<String, Object> saleEvent) {
        String saleCode = (String) saleEvent.get("saleCode");
        Long saleId = ((Number) saleEvent.get("saleId")).longValue();

        log.info("📄 Gerando NFC-e para venda: {}", saleCode);

        List<Map<String, Object>> rawItems = (List<Map<String, Object>>) saleEvent.get("items");
        AtomicInteger seq = new AtomicInteger(1);

        List<NfceItem> items = rawItems.stream().map(i -> NfceItem.builder()
                .itemNumber(seq.getAndIncrement())
                .productCode((String) i.get("productCode"))
                .productName((String) i.get("productName"))
                .ncm((String) i.get("ncm"))
                .cfop((String) i.get("cfop"))
                .unit((String) i.get("unit"))
                .quantity(((Number) i.get("quantity")).intValue())
                .unitPrice(new BigDecimal(i.get("unitPrice").toString()))
                .subtotal(new BigDecimal(i.get("subtotal").toString()))
                .taxRate(new BigDecimal(i.get("taxRate").toString()))
                .taxAmount(new BigDecimal(i.get("taxAmount").toString()))
                .build()
        ).collect(Collectors.toList());

        BigDecimal total = new BigDecimal(saleEvent.get("total").toString());
        BigDecimal taxTotal = items.stream()
                .map(NfceItem::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Nfce nfce = Nfce.create(saleCode, saleId,
                (String) saleEvent.get("customerDocument"),
                (String) saleEvent.get("customerName"),
                (String) saleEvent.get("customerEmail"),
                (String) saleEvent.get("paymentMethod"),
                items, total, taxTotal);

        // Simula autorização SEFAZ (em produção integraria com webservice real)
        try {
            String qrCodeBase64 = generateQrCode(nfce.getAccessKey());
            String qrCodeUrl = "https://nfce.fazenda.sp.gov.br/consulta?chNFe=" + nfce.getAccessKey();
            String protocol = "135" + System.currentTimeMillis();
            String xmlContent = generateXml(nfce, items);

            // Gera PDF DANFE
            byte[] danfePdf = danfeGenerator.generate(nfce, items);
            String danfeBase64 = Base64.getEncoder().encodeToString(danfePdf);

            nfce.authorize(protocol, xmlContent, qrCodeUrl, qrCodeBase64, danfeBase64);
            log.info("✅ NFC-e autorizada: accessKey={}", nfce.getAccessKey());
        } catch (Exception e) {
            log.error("❌ Erro ao autorizar NFC-e: {}", e.getMessage(), e);
            nfce.reject(e.getMessage());
        }

        Nfce saved = nfceRepository.save(nfce);

        if (saved.getStatus() == Nfce.NfceStatus.AUTHORIZED) {
            // Publica evento → Notification Service envia email
            eventPublisher.publishNfceAuthorized(saved);
            log.info("📨 Evento nfce.authorized publicado para email: {}", saved.getCustomerEmail());
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Nfce findById(Long id) {
        return nfceRepository.findById(id)
                .orElseThrow(() -> new NfceNotFoundException("NFC-e não encontrada: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Nfce findBySaleCode(String saleCode) {
        return nfceRepository.findBySaleCode(saleCode)
                .orElseThrow(() -> new NfceNotFoundException("NFC-e não encontrada para venda: " + saleCode));
    }

    @Override
    @Transactional(readOnly = true)
    public Nfce findByAccessKey(String accessKey) {
        return nfceRepository.findByAccessKey(accessKey)
                .orElseThrow(() -> new NfceNotFoundException("NFC-e não encontrada: " + accessKey));
    }

    @Override
    public byte[] getDanfePdf(Long id) {
        Nfce nfce = findById(id);
        if (nfce.getDanfeUrl() != null) {
            return Base64.getDecoder().decode(nfce.getDanfeUrl());
        }
        return danfeGenerator.generate(nfce, nfce.getItems());
    }

    private String generateQrCode(String content) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        } catch (Exception e) {
            log.warn("Erro ao gerar QR Code: {}", e.getMessage());
            return "";
        }
        return Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
    }

    private String generateXml(Nfce nfce, List<NfceItem> items) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<nfeProc xmlns=\"http://www.portalfiscal.inf.br/nfe\">\n");
        xml.append("  <NFe>\n");
        xml.append("    <infNFe Id=\"NFe").append(nfce.getAccessKey()).append("\">\n");
        xml.append("      <ide>\n");
        xml.append("        <cUF>35</cUF>\n");
        xml.append("        <mod>65</mod>\n");  // 65 = NFC-e
        xml.append("        <serie>").append(nfce.getSeries()).append("</serie>\n");
        xml.append("        <nNF>").append(nfce.getNfceNumber()).append("</nNF>\n");
        xml.append("        <dhEmi>").append(nfce.getIssuedAt()).append("</dhEmi>\n");
        xml.append("        <tpNF>1</tpNF>\n");  // 1 = Saída
        xml.append("        <idDest>1</idDest>\n");
        xml.append("        <tpImp>4</tpImp>\n");  // 4 = DANFE NFC-e
        xml.append("        <tpEmis>1</tpEmis>\n");
        xml.append("      </ide>\n");
        xml.append("      <emit>\n");
        xml.append("        <CNPJ>").append(nfce.getIssuerCnpj()).append("</CNPJ>\n");
        xml.append("        <xNome>").append(nfce.getIssuerName()).append("</xNome>\n");
        xml.append("        <xFant>").append(nfce.getIssuerFantasyName()).append("</xFant>\n");
        xml.append("      </emit>\n");
        xml.append("      <dest>\n");
        if (nfce.getCustomerDocument() != null) {
            xml.append("        <CPF>").append(nfce.getCustomerDocument()).append("</CPF>\n");
        }
        xml.append("        <xNome>").append(nfce.getCustomerName() != null ? nfce.getCustomerName() : "CONSUMIDOR").append("</xNome>\n");
        xml.append("      </dest>\n");

        int itemNum = 1;
        for (NfceItem item : items) {
            xml.append("      <det nItem=\"").append(itemNum++).append("\">\n");
            xml.append("        <prod>\n");
            xml.append("          <cProd>").append(item.getProductCode()).append("</cProd>\n");
            xml.append("          <xProd>").append(item.getProductName()).append("</xProd>\n");
            xml.append("          <NCM>").append(item.getNcm() != null ? item.getNcm() : "00000000").append("</NCM>\n");
            xml.append("          <CFOP>").append(item.getCfop() != null ? item.getCfop() : "5102").append("</CFOP>\n");
            xml.append("          <uCom>").append(item.getUnit()).append("</uCom>\n");
            xml.append("          <qCom>").append(item.getQuantity()).append("</qCom>\n");
            xml.append("          <vUnCom>").append(item.getUnitPrice()).append("</vUnCom>\n");
            xml.append("          <vProd>").append(item.getSubtotal()).append("</vProd>\n");
            xml.append("        </prod>\n");
            xml.append("        <imposto>\n");
            xml.append("          <vTotTrib>").append(item.getTaxAmount()).append("</vTotTrib>\n");
            xml.append("        </imposto>\n");
            xml.append("      </det>\n");
        }

        xml.append("      <total>\n");
        xml.append("        <ICMSTot>\n");
        xml.append("          <vProd>").append(nfce.getProductTotal()).append("</vProd>\n");
        xml.append("          <vDesc>").append(nfce.getDiscountTotal()).append("</vDesc>\n");
        xml.append("          <vNF>").append(nfce.getTotal()).append("</vNF>\n");
        xml.append("        </ICMSTot>\n");
        xml.append("      </total>\n");
        xml.append("      <pag>\n");
        xml.append("        <detPag>\n");
        xml.append("          <vPag>").append(nfce.getPaymentAmount()).append("</vPag>\n");
        xml.append("        </detPag>\n");
        xml.append("      </pag>\n");
        xml.append("    </infNFe>\n");
        xml.append("  </NFe>\n");
        xml.append("</nfeProc>");
        return xml.toString();
    }
}
