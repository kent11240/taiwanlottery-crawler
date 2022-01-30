package com.taiwanlottery.crawler.controller;

import com.taiwanlottery.crawler.exception.TicketNotFoundException;
import com.taiwanlottery.crawler.model.Ticket;
import com.taiwanlottery.crawler.response.TicketResponse;
import com.taiwanlottery.crawler.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/ticket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public List<TicketResponse> get() {
        return ticketService.getAll().stream()
                .map(ticket -> TicketResponse.of(ticket, ticketService.statistics(ticket)))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{ticketId}", produces = "application/json")
    public TicketResponse get(@PathVariable int ticketId) throws TicketNotFoundException {
        Ticket ticket = ticketService.get(ticketId);
        return TicketResponse.of(ticket, ticketService.statistics(ticket));
    }
}
