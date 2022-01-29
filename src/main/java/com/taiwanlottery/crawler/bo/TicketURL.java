package com.taiwanlottery.crawler.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketURL {
    private int id;
    private String name;
    private long bet;
    private String url;
}
