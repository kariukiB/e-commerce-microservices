package com.kariuki.ecommerce.email;

import com.kariuki.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kariuki.ecommerce.email.EmailTemplates.ORDER_CONFIRMATION;
import static com.kariuki.ecommerce.email.EmailTemplates.PAYMENT_CONFIRMATION;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.mail.javamail.MimeMessageHelper.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            String orderReference,
            BigDecimal amount
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_RELATED, UTF_8.name());
        mimeMessageHelper.setFrom("sales@ecommerce.com");
        final String templateName = PAYMENT_CONFIRMATION.getTemplate();

        Map<String, Object> model = new HashMap<>();
        model.put("customerName", customerName);
        model.put("orderReference", orderReference);
        model.put("amount", amount);

        Context context = new Context();
        context.setVariables(model);
        mimeMessageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());
        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate, true);
            mimeMessageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("Email sent successfully to {} with template {}", destinationEmail, htmlTemplate);
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Async
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            String orderReference,
            BigDecimal amount,
            List<Product> products
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_RELATED, UTF_8.name());
        mimeMessageHelper.setFrom("sales@ecommerce.com");
        final String templateName = ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> model = new HashMap<>();
        model.put("customerName", customerName);
        model.put("orderReference", orderReference);
        model.put("amount", amount);
        model.put("products", products);

        Context context = new Context();
        context.setVariables(model);
        mimeMessageHelper.setSubject(ORDER_CONFIRMATION.getSubject());
        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate, true);
            mimeMessageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info("Email sent to {} with template {}", destinationEmail, htmlTemplate);
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
