package com.taiwanlottery.crawler.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {
    private double winRate;
    private double breakEvenRate;
    private double earningRate;
    private double expectedValue;
    private double expectedValueRate;
    private double happyRate;
}
