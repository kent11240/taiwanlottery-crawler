package com.taiwanlottery.crawler.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.taiwanlottery.crawler.exception.TicketNotFoundException;
import com.taiwanlottery.crawler.model.Prize;
import com.taiwanlottery.crawler.model.Statistics;
import com.taiwanlottery.crawler.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final LoadingCache<String, List<Ticket>> cache;

    public TicketService(CrawlerService crawlerService, Caffeine<Object, Object> caffeine) {
        this.cache = caffeine.build(key -> crawlerService.crawlAll());
    }

    public List<Ticket> getAll() {
        return cache.get("tickets");
    }

    public Ticket get(int ticketId) throws TicketNotFoundException {
        return cache.get("tickets").stream()
                .filter(ticket -> ticket.getId() == ticketId)
                .findFirst()
                .orElseThrow(TicketNotFoundException::new);
    }

    public Statistics statistics(Ticket ticket) {
        return Statistics.builder()
                .winRate(calculateWinRate(ticket))
                .earningRate(calculateEarningRate(ticket))
                .expectedValue(calculateExpectedValue(ticket))
                .build();
    }

    private double calculateWinRate(Ticket ticket) {
        double totalPrizeAmount = ticket.getPrizes().stream().mapToLong(Prize::getAmount).sum();
        double totalAmount = ticket.getTotalAmount();
        return totalPrizeAmount / totalAmount;
    }

    private double calculateEarningRate(Ticket ticket) {
        double totalEarningAmount = ticket.getPrizes().stream()
                .filter(prize -> prize.getWin() > ticket.getBet())
                .mapToLong(Prize::getAmount).sum();
        double totalAmount = ticket.getTotalAmount();
        return totalEarningAmount / totalAmount;
    }

    private double calculateExpectedValue(Ticket ticket) {
        return ticket.getPrizes().stream()
                .mapToDouble(prize ->
                        (double) prize.getWin() * (double) prize.getAmount() / (double) ticket.getTotalAmount())
                .sum();
    }
}
