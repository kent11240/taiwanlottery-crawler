package com.taiwanlottery.crawler.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.taiwanlottery.crawler.exception.CrawlerServerException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class SheetService {
    private static final String SPREADSHEET_ID = "1uz8Y5XCfl-7yicwlVr-MQBVQQF2a5bcoG4zV6_X-VM0";

    private final GoogleAuthorizeService googleAuthorizeService;

    private Sheets sheets;

    public SheetService(GoogleAuthorizeService googleAuthorizeService) {
        this.googleAuthorizeService = googleAuthorizeService;
    }

    public void login() throws CrawlerServerException {
        if (sheets != null){
            throw new CrawlerServerException("already login.");
        }

        Credential authorize = googleAuthorizeService.authorize();
        this.sheets = constructSheets(authorize);
    }

    public void update(String range, String content) throws CrawlerServerException {
        ValueRange body = new ValueRange().setValues(List.of(List.of(content)));

        try {
            sheets.spreadsheets().values()
                    .update(SPREADSHEET_ID, range, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
        } catch (IOException e) {
            throw new CrawlerServerException("update failed.", e);
        }
    }

    private Sheets constructSheets(Credential authorize) throws CrawlerServerException {
        try {
            return new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(), authorize)
                    .setApplicationName("Taiwan Lottery Crawler Sheet")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new CrawlerServerException("construct sheets failed.", e);
        }
    }
}
