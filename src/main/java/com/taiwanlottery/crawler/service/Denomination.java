package com.taiwanlottery.crawler.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Denomination {
    ONE_HUNDRED(100, 500),
    TWO_HUNDRED(200, 500),
    THREE_HUNDRED(300, 1000),
    FIVE_HUNDRED(500, 1000),
    ONE_THOUSAND(1000, 2000),
    TWO_THOUSAND(2000, 5000);

    private final long bet;
    private final long happyThreshold;

    public static Denomination of(long bet) {
        for (Denomination denomination : values()) {
            if (denomination.bet == bet) {
                return denomination;
            }
        }
        throw new IllegalArgumentException("Invalid bet: " + bet);
    }
}
