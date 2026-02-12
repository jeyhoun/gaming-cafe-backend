package az.gaming_cafe.service;

import az.gaming_cafe.model.dto.request.EmailRequestDto;

public interface EmailService {

    void send(EmailRequestDto request);
    void sendTicketEmail(Long ticketId, String userEmail, String category, String priority, String message);
    void sendPasswordResetEmail(String email, String resetLink);
}
