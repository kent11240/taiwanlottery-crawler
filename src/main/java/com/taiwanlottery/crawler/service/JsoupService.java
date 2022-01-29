package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.exception.DataAccessException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsoupService {

    public Document get(String url) throws DataAccessException {
        try {
            return Jsoup.connect(url)
                    .maxBodySize(Integer.MAX_VALUE)
                    .get();
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }
}
