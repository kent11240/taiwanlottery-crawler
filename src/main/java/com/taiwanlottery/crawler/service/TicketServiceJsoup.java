package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.bo.TicketURL;
import com.taiwanlottery.crawler.exception.DataAccessException;
import com.taiwanlottery.crawler.util.StringUtils;
import com.taiwanlottery.crawler.vo.Prize;
import com.taiwanlottery.crawler.vo.Ticket;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceJsoup implements TicketService {
    private static final String TICKETS_HOME_URL = "https://www.taiwanlottery.com.tw/info/instant/sale.aspx";
    private static final int MAX_TICKETS_SIZE = 40;

    private final JsoupService jsoupService;

    public TicketServiceJsoup(JsoupService jsoupService) {
        this.jsoupService = jsoupService;
    }

    public List<Ticket> fetchAll() throws DataAccessException {
        List<TicketURL> ticketURLs = fetchTicketsURLs();

        List<Ticket> tickets = new ArrayList<>();
        for (TicketURL ticketURL : ticketURLs) {
            try {
                tickets.add(fetchSingle(ticketURL));
            } catch (DataAccessException ignored) {
            }
        }

        return tickets;
    }

    private List<TicketURL> fetchTicketsURLs() throws DataAccessException {
        Document document = jsoupService.get(TICKETS_HOME_URL);

        Elements elements = document.select(".tableFull a:not(.txt_link)");

        return elements.stream()
                .limit(MAX_TICKETS_SIZE)
                .map(element -> TicketURL.builder()
                        .id(Integer.parseInt(element.attr("href").split("#")[1]))
                        .url(element.attr("href"))
                        .build())
                .collect(Collectors.toList());
    }

    private Ticket fetchSingle(TicketURL ticketURL) throws DataAccessException {
        Document document = jsoupService.get(ticketURL.getUrl());

        Ticket ticket = new Ticket();
        ticket.setId(ticketURL.getId());

        Element ticketDiv = document.select("#" + ticketURL.getId()).parents().get(1);

        String name = ticketDiv.select("p").get(1).text();
        ticket.setName(name.substring(name.indexOf("：") + 1, name.indexOf(" ")));

        Elements prizeTds = ticketDiv.select("tbody tr:not(.td_hm) td");

        List<Prize> prizes = new ArrayList<>();
        for (int i = 0; i < prizeTds.size(); i += 2) {
            String winText = prizeTds.get(i).text();
            String amountText = prizeTds.get(i + 1).text();

            if (winText.equals("發行張數")) {
                ticket.setTotalAmount(Long.parseLong(StringUtils.removeNonDigitCharacters(amountText)));
                break;
            } else if (!winText.isEmpty()) {
                prizes.add(Prize.builder()
                        .win(Long.parseLong(StringUtils.parseWinString(winText)))
                        .amount(Long.parseLong(StringUtils.removeNonDigitCharacters(amountText)))
                        .build());
            }
        }

        ticket.setPrizes(prizes.stream()
                .sorted(Comparator.comparingLong(Prize::getWin))
                .collect(Collectors.toList()));

        return ticket;
    }
}
