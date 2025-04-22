package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        try {
            logger.info("Attempting to send email to: {}", to);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("banik.rajdeep1056@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            logger.info("Attempting to send password reset email to: {}", to);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("banik.rajdeep1056@gmail.com"); // Add sender email
            message.setTo(to);
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, paste the token below back to the app:\n\n" + resetLink);
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}