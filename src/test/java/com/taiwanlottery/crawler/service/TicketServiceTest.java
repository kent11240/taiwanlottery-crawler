package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.exception.CrawlerException;
import org.junit.jupiter.api.Test;

class TicketServiceTest {
    @Test
    void statistics() throws CrawlerException {
        new CrawlerService().crawlAll()
                .forEach(ticket -> {
                    System.out.println(ticket);
                    System.out.println(new TicketService().statistics(ticket));
                });
    }
}