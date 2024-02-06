package com.taiwanlottery.crawler.model.official_api;

import lombok.Data;

@Data
public class Scratch {
    private int gameVol;
    private String scratchName;
    private long money;
    private String newsId;
}
