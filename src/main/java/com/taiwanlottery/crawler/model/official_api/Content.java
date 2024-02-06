package com.taiwanlottery.crawler.model.official_api;

import lombok.Data;

import java.util.List;

@Data
public class Content {
    private List<Scratch> resultList;
}
