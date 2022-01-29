package com.taiwanlottery.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiwanlottery.crawler.exception.DataAccessException;
import com.taiwanlottery.crawler.vo.Ticket;
import org.junit.jupiter.api.Test;

import java.util.List;

class TicketServiceJsoupTest {
    @Test
    void get() throws DataAccessException, JsonProcessingException {
        List<Ticket> tickets = new TicketServiceJsoup(new JsoupService()).fetchAll();
        System.out.println(new ObjectMapper().writeValueAsString(tickets));
    }
}