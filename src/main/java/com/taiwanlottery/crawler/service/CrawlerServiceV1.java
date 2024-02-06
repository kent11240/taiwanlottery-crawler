package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.exception.CrawlerServerException;
import com.taiwanlottery.crawler.model.Prize;
import com.taiwanlottery.crawler.model.RawTicket;
import com.taiwanlottery.crawler.model.Ticket;
import com.taiwanlottery.crawler.util.StringUtils;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerServiceV1 extends CrawlerService {
    private static final String TICKETS_HOME_URL = "https://www.taiwanlottery.com.tw/info/instant/sale.aspx";
    private static final int MAX_TICKETS_SIZE = 40;

    private final JsoupService jsoupService;

    public CrawlerServiceV1(Logger logger, JsoupService jsoupService) {
        super(logger);
        this.jsoupService = jsoupService;
    }

    @Override
    List<RawTicket> fetchTicketsURLs() throws CrawlerServerException {
        Elements tableRows = jsoupService.connect(TICKETS_HOME_URL).select(".tableFull tr");

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

    @Override
    Ticket completeTicket(RawTicket rawTicket) throws CrawlerServerException {
        Ticket ticket = new Ticket();
        ticket.setId(rawTicket.getId());
        ticket.setName(rawTicket.getName());
        ticket.setBet(rawTicket.getBet());

        Elements prizeTableData = jsoupService.connect(rawTicket.getUrl())
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
}
