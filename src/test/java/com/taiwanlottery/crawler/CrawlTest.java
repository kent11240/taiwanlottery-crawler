package com.taiwanlottery.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiwanlottery.crawler.handler.TicketHandler;
import com.taiwanlottery.crawler.service.MockyIoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CrawlTest {

    @Autowired
    private TicketHandler ticketHandler;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockyIoService mockyIoService;

    @Test
    void run() {
        try {
            String content = objectMapper.writeValueAsString(ticketHandler.get());
            System.out.println(mockyIoService.send(content));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}