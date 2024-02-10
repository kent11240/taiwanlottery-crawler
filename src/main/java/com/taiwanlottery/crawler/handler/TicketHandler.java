package com.taiwanlottery.crawler.handler;

import com.taiwanlottery.crawler.exception.TicketNotFoundException;
import com.taiwanlottery.crawler.model.Ticket;
import com.taiwanlottery.crawler.dto.TicketResponse;
import com.taiwanlottery.crawler.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketHandler {
    private final TicketService ticketService;

    public TicketHandler(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public List<TicketResponse> get() {
        return ticketService.getAll().stream()
                .map(ticket -> TicketResponse.of(ticket, ticketService.statistics(ticket)))
                .collect(Collectors.toList());
    }

    public TicketResponse get(int ticketId) throws TicketNotFoundException {
        Ticket ticket = ticketService.get(ticketId);
        return TicketResponse.of(ticket, ticketService.statistics(ticket));
    }
}
