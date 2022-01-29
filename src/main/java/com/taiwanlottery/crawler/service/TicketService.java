package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.exception.DataAccessException;
import com.taiwanlottery.crawler.vo.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> fetchAll() throws DataAccessException;
}
