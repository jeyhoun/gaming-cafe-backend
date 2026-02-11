package az.gaming_cafe.service.impl;

import az.gaming_cafe.exception.ApplicationException;
import az.gaming_cafe.exception.data.ErrorCode;
import az.gaming_cafe.model.dto.request.CreateTicketRequestDto;
import az.gaming_cafe.model.dto.request.EmailRequestDto;
import az.gaming_cafe.model.entity.TicketEntity;
import az.gaming_cafe.model.entity.UserEntity;
import az.gaming_cafe.model.enums.TicketCategory;
import az.gaming_cafe.model.enums.TicketPriority;
import az.gaming_cafe.repository.TicketRepository;
import az.gaming_cafe.repository.UserRepository;
import az.gaming_cafe.service.EmailService;
import az.gaming_cafe.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${support.email}")
    private String to;

    public TicketServiceImpl(TicketRepository ticketRepository, EmailService emailService, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Override
    public void create(CreateTicketRequestDto request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        TicketEntity ticket = new TicketEntity();
        ticket.setUser(user);
        ticket.setSubject(request.getSubject());
        ticket.setMessage(request.getMessage());
        ticket.setCategory(request.getCategory());
        ticket.setPriority(calculatePriority(request.getCategory()));

        ticketRepository.save(ticket);
        try {

            String html = loadTemplate("ticket.html")
                    .replace("{{ticketId}}", ticket.getId().toString())
                    .replace("{{userEmail}}", user.getEmail())
                    .replace("{{priority}}", ticket.getPriority().name())
                    .replace("{{message}}", request.getMessage());

            EmailRequestDto emailRequest = EmailRequestDto.builder()
                    .from(from)
                    .to(to)
                    .replyTo(user.getEmail())
                    .subject(request.getSubject())
                    .message(html)
                    .build();

            emailService.send(emailRequest);
            log.info("ActionLog.create Ticket creation email sent to {}", user.getEmail());

            //CHECKSTYLE:OFF
        } catch (Exception e) {
            log.error("ActionLog.create.error Failed to send email for TicketID: {}", ticket.getId(), e);
        }//CHECKSTYLE:ON
    }

    private TicketPriority calculatePriority(TicketCategory category) {
        return switch (category) {
            case TECHNICAL, HARDWARE -> TicketPriority.CRITICAL;
            case BILLING -> TicketPriority.HIGH;
            case GENERAL -> TicketPriority.MEDIUM;
            case OTHER -> TicketPriority.LOW;
        };
    }

    private String loadTemplate(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException("Template not found: " + path);
            }

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot load email template", e);
        }
    }
}
