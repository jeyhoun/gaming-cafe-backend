package az.gaming_cafe.service.impl;

import az.gaming_cafe.exception.ApplicationException;
import az.gaming_cafe.exception.data.ErrorCode;
import az.gaming_cafe.model.dto.request.CreateTicketRequestDto;
import az.gaming_cafe.model.entity.TicketEntity;
import az.gaming_cafe.model.entity.UserEntity;
import az.gaming_cafe.model.enums.TicketCategory;
import az.gaming_cafe.model.enums.TicketPriority;
import az.gaming_cafe.repository.TicketRepository;
import az.gaming_cafe.repository.UserRepository;
import az.gaming_cafe.service.EmailService;
import az.gaming_cafe.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             EmailService emailService,
                             UserRepository userRepository) {
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
            emailService.sendTicketEmail(
                    ticket.getId(),
                    user.getEmail(),
                    ticket.getCategory().name(),
                    ticket.getPriority().name(),
                    request.getMessage()
            );
            log.info("ActionLog.create Ticket email sent");
            //CHECKSTYLE:OFF
        } catch (Exception e) {
            log.error("ActionLog.create.error TicketID: {}", ticket.getId(), e);
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
}
