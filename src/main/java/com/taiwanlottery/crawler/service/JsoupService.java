package com.taiwanlottery.crawler.service;

import com.taiwanlottery.crawler.exception.CrawlerServerException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Service
public class JsoupService {

    public Document connect(String url) throws CrawlerServerException {
        try {
            return Jsoup.connect(url)
                    .maxBodySize(Integer.MAX_VALUE)
                    .sslSocketFactory(socketFactory())
                    .get();
        } catch (IOException e) {
            throw new CrawlerServerException(e);
        }
    }

    public Document parse(String html) {
        return Jsoup.parse(html);
    }

    private SSLSocketFactory socketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }
}
