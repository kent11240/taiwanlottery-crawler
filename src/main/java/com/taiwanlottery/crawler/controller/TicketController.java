package com.taiwanlottery.crawler.controller;

import com.taiwanlottery.crawler.exception.TicketNotFoundException;
import com.taiwanlottery.crawler.handler.TicketHandler;
import com.taiwanlottery.crawler.response.TicketResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/ticket")
public class TicketController {

    private final TicketHandler ticketHandler;

    public TicketController(TicketHandler ticketHandler) {
        this.ticketHandler = ticketHandler;
    }

    @GetMapping(value = "/", produces = "application/json")
    public List<TicketResponse> get() {
        return ticketHandler.get();
    }

    @GetMapping(value = "/{ticketId}", produces = "application/json")
    public TicketResponse get(@PathVariable int ticketId) throws TicketNotFoundException {
        return ticketHandler.get(ticketId);
    }
}
