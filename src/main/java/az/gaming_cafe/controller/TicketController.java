package az.gaming_cafe.controller;

import az.gaming_cafe.model.dto.common.ApiResult;
import az.gaming_cafe.model.dto.request.CreateTicketRequestDto;
import az.gaming_cafe.service.TicketService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ApiResult<Void> create(@RequestBody CreateTicketRequestDto request) {
        ticketService.create(request);
        return ApiResult.ok();
    }
}
