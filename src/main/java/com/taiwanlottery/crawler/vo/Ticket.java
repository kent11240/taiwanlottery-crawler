package com.taiwanlottery.crawler.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Ticket {
    private int id;
    private String name;
    private long bet;
    private List<Prize> prizes;
    private long totalAmount;
}