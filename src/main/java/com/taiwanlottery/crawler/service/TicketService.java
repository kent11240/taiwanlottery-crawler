package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.vo.Prize;
import com.taiwanlottery.crawler.vo.Statistics;
import com.taiwanlottery.crawler.vo.Ticket;

public class TicketService {

    public Statistics statistics(Ticket ticket) {
        return Statistics.builder()
                .winRate(calculateWinRate(ticket))
                .earningRate(calculateEarningRate(ticket))
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
        return totalEarningAmount/ totalAmount;
    }
}
