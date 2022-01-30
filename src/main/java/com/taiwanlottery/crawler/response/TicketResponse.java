package com.taiwanlottery.crawler.response;

import com.taiwanlottery.crawler.model.Prize;
import com.taiwanlottery.crawler.model.Statistics;
import com.taiwanlottery.crawler.model.Ticket;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TicketResponse {
    private int id;
    private String name;
    private long bet;
    private List<Prize> prizes;
    private long totalAmount;
    private Statistics statistics;

    public static TicketResponse of(Ticket ticket, Statistics statistics) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .name(ticket.getName())
                .bet(ticket.getBet())
                .prizes(ticket.getPrizes())
                .totalAmount(ticket.getTotalAmount())
                .statistics(statistics)
                .build();
    }
}
