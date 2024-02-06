package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.exception.CrawlerServerException;
import com.taiwanlottery.crawler.model.Prize;
import com.taiwanlottery.crawler.model.RawTicket;
import com.taiwanlottery.crawler.model.Ticket;
import com.taiwanlottery.crawler.model.official_api.Response;
import com.taiwanlottery.crawler.util.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CrawlerServiceV2 extends CrawlerService {

    private final JsoupService jsoupService;

    public CrawlerServiceV2(Logger logger, JsoupService jsoupService) {
        super(logger);
        this.jsoupService = jsoupService;
    }

    @Override
    List<RawTicket> fetchTicketsURLs() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.taiwanlottery.com/TLCAPIWeB/Instant/Result?ScratchName&Start_ListingDate&End_ListingDate&PageNum=1&PageSize=20&Type=1")
                .build();

        Response response = webClient.get()
                .retrieve()
                .bodyToMono(Response.class)
                .block();

        return Objects.requireNonNull(response).getContent().getResultList()
                .stream().map(scratch -> RawTicket.builder()
                        .id(scratch.getGameVol())
                        .name(scratch.getScratchName())
                        .bet(scratch.getMoney())
                        .url(String.format("https://www.taiwanlottery.com/news/news/%s", scratch.getNewsId()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    Ticket completeTicket(RawTicket rawTicket) throws CrawlerServerException {
        Ticket ticket = new Ticket();
        ticket.setId(rawTicket.getId());
        ticket.setName(rawTicket.getName());
        ticket.setBet(rawTicket.getBet());

        Document document = jsoupService.connect(rawTicket.getUrl());
        String iframeSrc = document.select("#lotto-news-iframe").attr("src");

        Document parse = jsoupService.parse(iframeSrc);

        Elements prizeTableData = parse
                .select("#" + rawTicket.getId())
                .parents().get(1)
                .select("tbody tr:not(.td_hm) td");

        List<Prize> prizes = new ArrayList<>();
        for (int i = 0; i < prizeTableData.size(); i += 2) {
            String winSource = prizeTableData.get(i).text();
            String amountSource = prizeTableData.get(i + 1).text();

            if (winSource.equals("發行張數")) {
                ticket.setTotalAmount(Long.parseLong(StringUtils.removeNonDigitCharacters(amountSource)));
                break;
            } else if (!winSource.isEmpty()) {
                String[] winArray = Arrays.stream(winSource.split(" ")).filter(winText -> winText.contains("NT$")).toArray(String[]::new);
                String[] amountArray = amountSource.split(" ");

                for (int j = 0; j < winArray.length; j++) {
                    prizes.add(Prize.builder()
                            .win(Long.parseLong(StringUtils.parseWinString(winArray[j])))
                            .amount(Long.parseLong(StringUtils.removeNonDigitCharacters(amountArray[j])))
                            .build());
                }
            }
        }

        ticket.setPrizes(prizes.stream()
                .sorted(Comparator.comparingLong(Prize::getWin))
                .collect(Collectors.toList()));

        return ticket;
    }
}
