package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.exception.CrawlerServerException;
import com.taiwanlottery.crawler.dto.MockyIoRequest;
import com.taiwanlottery.crawler.dto.MockyIoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class MockyIoService {
    private static final int STATUS = 200;
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARSET = "UTF-8";
    private static final String SECRET = "gPX0DpukYnltQjD7fLamo0wY2nzylaAB0zJH";
    private static final String EXPIRATION = "never";
    private static final String MOCKY_IO_URL = "https://api.mocky.io/api/mock";

    private final RestTemplate restTemplate;

    public MockyIoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String send(String content) throws CrawlerServerException {
        MockyIoRequest request = MockyIoRequest.builder()
                .status(STATUS)
                .content(content)
                .contentType(CONTENT_TYPE)
                .charset(CHARSET)
                .secret(SECRET)
                .expiration(EXPIRATION)
                .build();

        MockyIoResponse mockyIoResponse = restTemplate.postForObject(MOCKY_IO_URL, request, MockyIoResponse.class);

        return Optional.ofNullable(mockyIoResponse).map(MockyIoResponse::getLink)
                .orElseThrow(() -> new CrawlerServerException("send to mocky.io failed."));
    }
}
