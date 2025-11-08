package com.ZerodaySolution.Billing.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//class to do the logic of sending an activation emaillink

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Async
    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public void sendMail(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Mail server connection failed. Failed messages: " + e.getMessage());

        }

    }

    @Recover
    public void recover(Exception e, String to, String subject, String body) {
        // Log the failure or handle it gracefully
        System.err.println("Failed to send email after retries: " + e.getMessage());
    }
}
