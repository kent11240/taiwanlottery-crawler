package com.taiwanlottery.crawler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MockyIoRequest {
    private int status;
    private String content;
    @JsonProperty("content_type")
    private String contentType;
    private String charset;
    private String secret;
    private String expiration;
}
