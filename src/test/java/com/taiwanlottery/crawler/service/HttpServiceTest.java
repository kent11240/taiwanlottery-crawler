package com.taiwanlottery.crawler.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

class HttpServiceTest {
    private HttpService sut;

    @BeforeEach
    void setUp() {
        sut = new HttpService(HttpClient.newBuilder().build());
    }

    @Test
    void get() throws IOException, InterruptedException {
        System.out.println(sut.get());
    }
}