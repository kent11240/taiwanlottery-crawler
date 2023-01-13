package com.taiwanlottery.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiwanlottery.crawler.handler.TicketHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GetAllTicketTest {

    @Autowired
    private TicketHandler ticketHandler;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void run() {
        try {
            System.out.println(objectMapper.writeValueAsString(ticketHandler.get()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}