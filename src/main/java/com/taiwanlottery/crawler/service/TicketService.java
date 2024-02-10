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

    public TicketService(CrawlerServiceV2 crawlerService, Caffeine<Object, Object> caffeine) {
        this.cache = caffeine.build(key -> crawlerService.crawlAll());
    }

    public List<Ticket> getAll() {
        return cache.get("tickets");
    }

    public Ticket get(int ticketId) throws TicketNotFoundException {
        return cache.get("tickets").stream()
                .filter(ticket -> ticket.getId() == ticketId)
                .findFirst()
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    public Statistics statistics(Ticket ticket) {
        double expectedValue = calculateExpectedValue(ticket);
        return Statistics.builder()
                .winRate(calculateWinRate(ticket))
                .breakEvenRate(calculateBreakEvenRate(ticket))
                .earningRate(calculateEarningRate(ticket))
                .expectedValue(expectedValue)
                .expectedValueRate(expectedValue / ticket.getBet())
                .happyRate(calculateHappyRate(ticket))
                .build();
    }

    private double calculateWinRate(Ticket ticket) {
        double totalPrizeAmount = ticket.getPrizes().stream().mapToLong(Prize::getAmount).sum();
        double totalAmount = ticket.getTotalAmount();
        return totalPrizeAmount / totalAmount;
    }

    private double calculateBreakEvenRate(Ticket ticket) {
        double totalEarningAmount = ticket.getPrizes().stream()
                .filter(prize -> prize.getWin() >= ticket.getBet())
                .mapToLong(Prize::getAmount).sum();
        double totalAmount = ticket.getTotalAmount();
        return totalEarningAmount / totalAmount;
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

    private double calculateHappyRate(Ticket ticket) {
        double totalHappyAmount = ticket.getPrizes().stream()
                .filter(prize -> prize.getWin() >= Denomination.of(ticket.getBet()).getHappyThreshold()
                        && prize.getWin() <= 5000)
                .mapToLong(Prize::getAmount).sum();
        double totalAmount = ticket.getTotalAmount();
        return totalHappyAmount / totalAmount;
    }
}
