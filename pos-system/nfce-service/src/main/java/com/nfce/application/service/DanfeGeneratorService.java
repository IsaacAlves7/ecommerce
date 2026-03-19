package com.nfce.application.service;

import com.nfce.domain.model.Nfce;
import com.nfce.domain.model.NfceItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Gera o DANFE NFC-e em formato HTML (convertível a PDF via ferramenta externa).
 * Em produção, integrar com biblioteca como iText ou JasperReports.
 */
@Slf4j
@Service
public class DanfeGeneratorService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public byte[] generate(Nfce nfce, List<NfceItem> items) {
        String html = buildDanfeHtml(nfce, items);
        return html.getBytes();
    }

    private String buildDanfeHtml(Nfce nfce, List<NfceItem> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html lang='pt-BR'><head><meta charset='UTF-8'>");
        sb.append("<title>DANFE NFC-e</title>");
        sb.append("<style>");
        sb.append("* { font-family: monospace; font-size: 11px; margin: 0; padding: 0; }");
        sb.append("body { width: 80mm; padding: 4px; }");
        sb.append(".center { text-align: center; }");
        sb.append(".bold { font-weight: bold; }");
        sb.append(".line { border-top: 1px dashed #000; margin: 4px 0; }");
        sb.append("table { width: 100%; border-collapse: collapse; }");
        sb.append("td { padding: 1px 2px; }");
        sb.append(".right { text-align: right; }");
        sb.append("</style></head><body>");

        // Cabeçalho
        sb.append("<div class='center bold'>").append(nfce.getIssuerFantasyName()).append("</div>");
        sb.append("<div class='center'>").append(nfce.getIssuerName()).append("</div>");
        sb.append("<div class='center'>CNPJ: ").append(formatCnpj(nfce.getIssuerCnpj())).append("</div>");
        sb.append("<div class='center'>").append(nfce.getIssuerAddress()).append("</div>");
        sb.append("<div class='line'></div>");
        sb.append("<div class='center bold'>NOTA FISCAL DE CONSUMIDOR ELETRÔNICA</div>");
        sb.append("<div class='center'>NFC-e | Série: ").append(nfce.getSeries())
          .append(" | Nº: ").append(nfce.getNfceNumber()).append("</div>");
        sb.append("<div class='center'>Emissão: ")
          .append(nfce.getIssuedAt() != null ? nfce.getIssuedAt().format(FMT) : "--").append("</div>");
        sb.append("<div class='line'></div>");

        // Destinatário
        if (nfce.getCustomerName() != null) {
            sb.append("<div><b>Cliente:</b> ").append(nfce.getCustomerName()).append("</div>");
        }
        if (nfce.getCustomerDocument() != null) {
            sb.append("<div><b>CPF:</b> ").append(formatCpf(nfce.getCustomerDocument())).append("</div>");
        }
        sb.append("<div class='line'></div>");

        // Itens
        sb.append("<table>");
        sb.append("<tr><td class='bold'>Produto</td><td class='right bold'>Qtd</td><td class='right bold'>Unit.</td><td class='right bold'>Total</td></tr>");
        sb.append("<tr><td colspan='4'><div class='line'></div></td></tr>");

        for (NfceItem item : items) {
            sb.append("<tr><td colspan='4'>").append(item.getProductName()).append("</td></tr>");
            sb.append("<tr>");
            sb.append("<td>").append(item.getProductCode()).append("</td>");
            sb.append("<td class='right'>").append(item.getQuantity()).append(" ").append(item.getUnit()).append("</td>");
            sb.append("<td class='right'>R$ ").append(fmt(item.getUnitPrice())).append("</td>");
            sb.append("<td class='right'>R$ ").append(fmt(item.getSubtotal())).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");

        // Totais
        sb.append("<div class='line'></div>");
        sb.append("<table>");
        sb.append("<tr><td>Subtotal:</td><td class='right'>R$ ").append(fmt(nfce.getProductTotal())).append("</td></tr>");
        sb.append("<tr><td>Desconto:</td><td class='right'>R$ ").append(fmt(nfce.getDiscountTotal())).append("</td></tr>");
        sb.append("<tr><td>Impostos:</td><td class='right'>R$ ").append(fmt(nfce.getTaxTotal())).append("</td></tr>");
        sb.append("<tr><td class='bold'>TOTAL:</td><td class='right bold'>R$ ").append(fmt(nfce.getTotal())).append("</td></tr>");
        sb.append("</table>");
        sb.append("<div class='line'></div>");

        // Pagamento
        sb.append("<div><b>Forma de Pagamento:</b> ").append(translatePayment(nfce.getPaymentMethod())).append("</div>");
        sb.append("<div><b>Valor Pago:</b> R$ ").append(fmt(nfce.getPaymentAmount())).append("</div>");
        sb.append("<div class='line'></div>");

        // QR Code
        sb.append("<div class='center bold'>Consulte pela chave de acesso:</div>");
        sb.append("<div class='center' style='font-size:9px;word-break:break-all;'>").append(nfce.getAccessKey()).append("</div>");
        if (nfce.getQrCodeBase64() != null && !nfce.getQrCodeBase64().isEmpty()) {
            sb.append("<div class='center'><img src='data:image/png;base64,")
              .append(nfce.getQrCodeBase64()).append("' width='150'/></div>");
        }
        sb.append("<div class='center' style='font-size:9px;'>").append(nfce.getQrCodeUrl() != null ? nfce.getQrCodeUrl() : "").append("</div>");

        if (nfce.getProtocol() != null) {
            sb.append("<div class='center'>Protocolo: ").append(nfce.getProtocol()).append("</div>");
        }
        sb.append("<div class='line'></div>");
        sb.append("<div class='center'>Obrigado pela preferência!</div>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private String fmt(java.math.BigDecimal v) {
        return v != null ? String.format("%.2f", v) : "0.00";
    }

    private String formatCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) return cnpj;
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    private String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String translatePayment(String method) {
        if (method == null) return "—";
        return switch (method) {
            case "CASH" -> "Dinheiro";
            case "CREDIT_CARD" -> "Cartão de Crédito";
            case "DEBIT_CARD" -> "Cartão de Débito";
            case "PIX" -> "PIX";
            case "VOUCHER" -> "Vale/Voucher";
            default -> method;
        };
    }
}
