package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.bo.RawTicket;
import com.taiwanlottery.crawler.exception.CrawlerException;
import com.taiwanlottery.crawler.util.StringUtils;
import com.taiwanlottery.crawler.vo.Prize;
import com.taiwanlottery.crawler.vo.Ticket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerService {
    private static final String TICKETS_HOME_URL = "https://www.taiwanlottery.com.tw/info/instant/sale.aspx";
    private static final int MAX_TICKETS_SIZE = 40;

    public List<Ticket> crawlAll() throws CrawlerException {
        List<RawTicket> rawTickets = fetchTicketsURLs();

        List<Ticket> tickets = new ArrayList<>();
        for (RawTicket rawTicket : rawTickets) {
            try {
                tickets.add(completeTicket(rawTicket));
            } catch (CrawlerException ignored) {
                System.out.println("bad ticket: " + rawTicket);
            }
        }

        return tickets;
    }

    private List<RawTicket> fetchTicketsURLs() throws CrawlerException {
        Elements tableRows = connect(TICKETS_HOME_URL).select(".tableFull tr");

        List<RawTicket> rawTickets = new ArrayList<>();
        for (int i = 0; i < MAX_TICKETS_SIZE * 4; i += 4) {
            String[] nameId = tableRows.get(i + 1).select("td:nth-child(1)").text().split("/");
            long bet = Long.parseLong(StringUtils.removeNonDigitCharacters(tableRows.get(i + 1).select("td:nth-child(2)").text()));
            Elements urls = tableRows.get(i + 2).select("td:nth-child(5) a");
            String url;
            if (urls.size() == 1) {
                url = urls.get(0).attr("href");
            } else {
                url = urls.get(1).attr("href");
            }

            rawTickets.add(RawTicket.builder()
                    .id(Integer.parseInt(nameId[1].trim()))
                    .name(nameId[0].trim())
                    .bet(bet)
                    .url(url)
                    .build());
        }

        return rawTickets;
    }

    private Ticket completeTicket(RawTicket rawTicket) throws CrawlerException {
        Ticket ticket = new Ticket();
        ticket.setId(rawTicket.getId());
        ticket.setName(rawTicket.getName());
        ticket.setBet(rawTicket.getBet());

        Elements prizeTableData = connect(rawTicket.getUrl())
                .select("#" + rawTicket.getId())
                .parents().get(1)
                .select("tbody tr:not(.td_hm) td");

        List<Prize> prizes = new ArrayList<>();
        for (int i = 0; i < prizeTableData.size(); i += 2) {
            String winText = prizeTableData.get(i).text();
            String amountText = prizeTableData.get(i + 1).text();

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

    private Document connect(String url) throws CrawlerException {
        try {
            return Jsoup.connect(url)
                    .maxBodySize(Integer.MAX_VALUE)
                    .get();
        } catch (IOException e) {
            throw new CrawlerException(e);
        }
    }
}
