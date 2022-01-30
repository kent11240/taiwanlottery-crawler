package com.taiwanlottery.crawler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {
    private double winRate;
    private double earningRate;
    private double expectedValue;
}
