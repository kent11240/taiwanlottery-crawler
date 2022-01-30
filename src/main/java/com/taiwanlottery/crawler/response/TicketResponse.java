package com.taiwanlottery.crawler.response;

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
    private double earningRate;
    private double expectedValue;
    private double fthRate;
    private double fthExpectedValue;

    public static TicketResponse of(Ticket ticket, Statistics statistics) {
        return TicketResponse.builder()
                .name(ticket.getName())
                .bet(ticket.getBet())
                .winRate(statistics.getWinRate())
                .earningRate(statistics.getEarningRate())
                .expectedValue(statistics.getExpectedValue())
                .fthRate(statistics.getFthRate())
                .fthExpectedValue(statistics.getFthExpectedValue())
                .build();
    }
}
