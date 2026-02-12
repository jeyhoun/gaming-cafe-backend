package az.gaming_cafe.service;

import az.gaming_cafe.model.dto.request.EmailRequestDto;

public interface EmailService {

    void send(EmailRequestDto request);

    void sendPasswordResetEmail(String email, String resetLink);
}
