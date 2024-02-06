package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.exception.CrawlerServerException;
import com.taiwanlottery.crawler.model.Prize;
import com.taiwanlottery.crawler.model.RawTicket;
import com.taiwanlottery.crawler.model.Ticket;
import com.taiwanlottery.crawler.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public abstract class CrawlerService {

    private final Logger logger;

    public List<Ticket> crawlAll() throws CrawlerServerException {
        List<RawTicket> rawTickets = fetchTicketsURLs();

        List<Ticket> tickets = new ArrayList<>();
        for (RawTicket rawTicket : rawTickets) {
            try {
                tickets.add(completeTicket(rawTicket));
            } catch (CrawlerServerException e) {
                logger.error("bad ticket: " + rawTicket, e);
            }
        }

        return tickets;
    }

    abstract List<RawTicket> fetchTicketsURLs() throws CrawlerServerException;

    abstract Ticket completeTicket(RawTicket rawTicket) throws CrawlerServerException;
}
