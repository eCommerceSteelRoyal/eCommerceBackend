package com.steelroyal.ecommercebackend.security.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.steelroyal.ecommercebackend.security.domain.service.EmailService;
import com.steelroyal.ecommercebackend.security.resource.request.EmailRequest;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    
    @Override
    @Async("asyncTaskExecutor")
    public void sendEmail(EmailRequest email){
        try{
            //System.out.println("Starting email send task for: " + email.getAddressee());
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email.getAddressee());
            helper.setSubject(email.getSubject());
            
            // Procesar la plantilla Thymeleaf
            Context context = new Context();
            context.setVariable("message", email.getMessage());
            String contentHTML = templateEngine.process("email", context);
            
            helper.setText(contentHTML, true);
            
            javaMailSender.send(message);
            System.out.println("Email sent to: " + email.getAddressee());
            //System.out.println("Message: " + email.getMessage());
        } catch (Exception e){
            //System.out.println("Error sending email to: " + email.getAddressee() + ", " + e);
            throw new RuntimeException("Error al enviar al correo: " + e.getMessage(), e);
        }
    }
}
