package com.taiwanlottery.crawler.response;

import com.taiwanlottery.crawler.model.Prize;
import com.taiwanlottery.crawler.model.Statistics;
import com.taiwanlottery.crawler.model.Ticket;
import com.taiwanlottery.crawler.util.StringUtils;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TicketResponse {
    private String id;
    private String name;
    private String bet;
    private String winRate;
    private String earningRate;
    private String expectedValue;

    public static TicketResponse of(Ticket ticket, Statistics statistics) {
        return TicketResponse.builder()
                .id(String.valueOf(ticket.getId()))
                .name(ticket.getName())
                .bet(StringUtils.commaFormat(ticket.getBet()))
                .winRate(StringUtils.percentageFormat(statistics.getWinRate()))
                .earningRate(StringUtils.percentageFormat(statistics.getEarningRate()))
                .expectedValue(StringUtils.commaPointFormat(statistics.getExpectedValue()))
                .build();
    }
}
