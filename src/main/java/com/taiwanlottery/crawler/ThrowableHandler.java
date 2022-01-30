package com.taiwanlottery.crawler;

import com.taiwanlottery.crawler.exception.CrawlerException;
import com.taiwanlottery.crawler.exception.TicketNotFoundException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ThrowableHandler {

    private final Logger logger;

    public ThrowableHandler(Logger logger) {
        this.logger = logger;
    }

    @ResponseBody
    @ExceptionHandler(CrawlerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    void handleException(CrawlerException e) {
        logger.error("crawl failed.", e);
    }

    @ResponseBody
    @ExceptionHandler(TicketNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handleException(TicketNotFoundException e) {
        logger.error(String.format("ticket: %d not found.", e.getTicketId()), e);
    }
}
