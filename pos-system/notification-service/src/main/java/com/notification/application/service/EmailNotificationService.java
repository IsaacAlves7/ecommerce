package com.notification.application.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.from:noreply@pos-system.com}")
    private String fromEmail;

    @SuppressWarnings("unchecked")
    public void sendNfceEmail(Map<String, Object> event) {
        String toEmail = (String) event.get("customerEmail");
        String customerName = (String) event.get("customerName");
        String saleCode = (String) event.get("saleCode");
        String nfceNumber = (String) event.get("nfceNumber");
        String accessKey = (String) event.get("accessKey");
        String qrCodeUrl = (String) event.get("qrCodeUrl");
        String qrCodeBase64 = (String) event.get("qrCodeBase64");
        BigDecimal total = new BigDecimal(event.get("total").toString());
        String paymentMethod = translatePayment((String) event.get("paymentMethod"));
        String issuedAt = (String) event.get("issuedAt");
        String issuerName = (String) event.get("issuerName");
        String issuerAddress = (String) event.get("issuerAddress");
        String danfeBase64 = (String) event.get("danfeBase64");
        List<Map<String, Object>> items = (List<Map<String, Object>>) event.get("items");

        try {
            // Build Thymeleaf context
            Context ctx = new Context();
            ctx.setVariable("customerName", customerName != null ? customerName : "Cliente");
            ctx.setVariable("saleCode", saleCode);
            ctx.setVariable("nfceNumber", nfceNumber);
            ctx.setVariable("accessKey", accessKey);
            ctx.setVariable("total", String.format("%.2f", total));
            ctx.setVariable("paymentMethod", paymentMethod);
            ctx.setVariable("issuedAt", issuedAt);
            ctx.setVariable("issuerName", issuerName);
            ctx.setVariable("issuerAddress", issuerAddress);
            ctx.setVariable("qrCodeUrl", qrCodeUrl);
            ctx.setVariable("qrCodeBase64", qrCodeBase64);
            ctx.setVariable("items", items);

            String htmlContent = templateEngine.process("nfce-email", ctx);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("✅ Sua Nota Fiscal Eletrônica - Venda " + saleCode);
            helper.setText(htmlContent, true);

            // Attach DANFE HTML if available
            if (danfeBase64 != null && !danfeBase64.isBlank()) {
                byte[] danfeBytes = java.util.Base64.getDecoder().decode(danfeBase64);
                helper.addAttachment("DANFE-NFC-e-" + nfceNumber + ".html",
                        new org.springframework.core.io.ByteArrayResource(danfeBytes));
            }

            mailSender.send(message);
            log.info("📧 Email NFC-e enviado: to={} saleCode={}", toEmail, saleCode);

        } catch (Exception e) {
            log.error("❌ Falha ao enviar email para {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar email: " + e.getMessage(), e);
        }
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
