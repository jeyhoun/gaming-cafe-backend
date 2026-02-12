package az.gaming_cafe.service.impl;

import az.gaming_cafe.model.dto.request.EmailRequestDto;
import az.gaming_cafe.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${support.email}")
    private String to;

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Override
    public void sendTicketEmail(Long ticketId, String userEmail, String category, String priority, String message) {
        Context context = new Context();
        context.setVariable("ticketId", ticketId);
        context.setVariable("userEmail", userEmail);
        context.setVariable("category", category);
        context.setVariable("priority", priority);
        context.setVariable("message", message);

        String htmlContent = templateEngine.process("ticket-notification", context);

        send(
                EmailRequestDto.builder()
                        .from(from)
                        .to(to)
                        .replyTo(userEmail)
                        .subject("New Ticket #" + ticketId)
                        .message(htmlContent)
                        .build()
        );
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetLink) {
        Context context = new Context();
        context.setVariable("resetLink", resetLink);

        String htmlContent = templateEngine.process("password-reset", context);

        send(
                EmailRequestDto.builder()
                        .from(from)
                        .to(email)
                        .replyTo(from)
                        .subject("Reset Your Password - Gaming Cafe")
                        .message(htmlContent)
                        .build()
        );
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
            log.info("ActionLog.send.end sent to email successfully");
        } catch (MessagingException | MailException e) {
            log.error("ActionLog.send.error Failed to send email", e);
        }
    }
}
