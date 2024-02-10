package com.taiwanlottery.crawler.dto;

import com.taiwanlottery.crawler.model.Statistics;
import com.taiwanlottery.crawler.model.Ticket;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponse {
    private String name;
    private long bet;
    private double winRate;
    private double breakEvenRate;
    private double earningRate;
    private double expectedValue;
    private double expectedValueRate;
    private double happyRate;

    public static TicketResponse of(Ticket ticket, Statistics statistics) {
        return TicketResponse.builder()
                .name(ticket.getName())
                .bet(ticket.getBet())
                .winRate(statistics.getWinRate())
                .breakEvenRate(statistics.getBreakEvenRate())
                .earningRate(statistics.getEarningRate())
                .expectedValue(statistics.getExpectedValue())
                .expectedValueRate(statistics.getExpectedValueRate())
                .happyRate(statistics.getHappyRate())
                .build();
    }
}
