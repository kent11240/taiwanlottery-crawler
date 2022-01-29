package com.taiwanlottery.crawler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiwanlottery.crawler.exception.CrawlerException;
import com.taiwanlottery.crawler.vo.Ticket;
import org.junit.jupiter.api.Test;

import java.util.List;

class CrawlerServiceTest {
    @Test
    void get() throws CrawlerException, JsonProcessingException {
        List<Ticket> tickets = new CrawlerService().crawlAll();
        System.out.println(new ObjectMapper().writeValueAsString(tickets));
    }
}