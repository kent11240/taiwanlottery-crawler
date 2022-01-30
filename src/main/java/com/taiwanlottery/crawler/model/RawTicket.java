package com.taiwanlottery.crawler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RawTicket {
    private int id;
    private String name;
    private long bet;
    private String url;
}
