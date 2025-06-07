package ru.itis.impl.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.itis.impl.exception.EmailSendingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailValidationService emailValidationService;

    @Value("${spring.mail.username}")
    private String username;

    public void sendReminder(String to, String subject, String content) {
        if (!emailValidationService.isValidEmail(to)) {
//            throw new EmailSendingException("Invalid email address: " + to);
            log.info("Invalid email address: {}", to);
            return;
        }

        if (!emailValidationService.isMailRuDomain(to)) {
//            throw new EmailSendingException("Only mail.ru domain is supported");
            log.info("Only mail.ru domain is supported");
            return;
        }

        if (!emailValidationService.isMailRuServerReachable()) {
//            throw new EmailSendingException("Mail.ru server is not reachable");
            log.info("Mail.ru server is not reachable");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException("Failed to send email: " + e.getMessage());
        }
    }
} 