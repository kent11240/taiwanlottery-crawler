package com.taiwanlottery.crawler.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Statistics {
    private double winRate;
    private double earningRate;

    @Override
    public String toString() {
        return "Statistics{" +
                "winRate=" + format(winRate) +
                ", earningRate=" + format(earningRate) +
                '}';
    }

    private String format(double d) {
        return String.format("%.2f%%", d * 100);
    }
}
