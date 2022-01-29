package com.taiwanlottery.crawler.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Prize {
    private long win;
    private long amount;
}
