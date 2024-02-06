package com.taiwanlottery.crawler.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.taiwanlottery.crawler.exception.CrawlerServerException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class GoogleAuthorizeService {
    private static final String OAUTH_JSON_FILE_PATH = "/google-sheets-client-secret.json";

    public Credential authorize() throws CrawlerServerException {
        GoogleClientSecrets clientSecrets = loadGoogleClientSecrets();
        List<String> scopes = List.of(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = constructFlow(clientSecrets, scopes);
        return authorize(flow);
    }

    private GoogleClientSecrets loadGoogleClientSecrets() throws CrawlerServerException {
        try (InputStream in = GoogleAuthorizeService.class.getResourceAsStream(OAUTH_JSON_FILE_PATH)) {
            return GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(Objects.requireNonNull(in)));
        } catch (IOException e) {
            throw new CrawlerServerException("load google client secret failed.", e);
        }
    }

    private GoogleAuthorizationCodeFlow constructFlow(GoogleClientSecrets clientSecrets, Collection<String> scopes) throws CrawlerServerException {
        try {
            return new GoogleAuthorizationCodeFlow
                    .Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), clientSecrets, scopes)
                    .setDataStoreFactory(new MemoryDataStoreFactory())
                    .setAccessType("offline")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new CrawlerServerException("new trusted transport failed.", e);
        }
    }

    private Credential authorize(GoogleAuthorizationCodeFlow flow) throws CrawlerServerException {
        try {
            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        } catch (IOException e) {
            throw new CrawlerServerException("authorize failed.", e);
        }
    }
}
