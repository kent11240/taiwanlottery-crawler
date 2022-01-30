package com.taiwanlottery.crawler.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {

    @Bean
    Caffeine<Object, Object> caffeine() {
        return Caffeine.newBuilder()
                .refreshAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterWrite(15, TimeUnit.MINUTES);
    }
}
