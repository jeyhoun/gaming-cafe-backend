package az.gaming_cafe.service.impl;

import az.gaming_cafe.model.dto.request.EmailRequestDto;
import az.gaming_cafe.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void send(EmailRequestDto request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(request.getFrom());
            helper.setTo(request.getTo());
            helper.setReplyTo(request.getReplyTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getMessage(), true);

            mailSender.send(message);
            log.info("Email sent to {}", request.getTo());
        } catch (MessagingException | MailException e) {
            log.error("Failed to send email from {}", request.getReplyTo(), e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetLink) {
        //todo
    }
}
