package az.gaming_cafe.service;

import az.gaming_cafe.model.dto.request.CreateTicketRequestDto;

public interface TicketService {

    void create(CreateTicketRequestDto request);
}
