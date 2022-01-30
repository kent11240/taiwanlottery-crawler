package com.taiwanlottery.crawler.exception;

import lombok.Getter;

@Getter
public class TicketNotFoundException extends Exception {

    private final int ticketId;

    public TicketNotFoundException(int ticketId) {
        this.ticketId = ticketId;
    }
}
