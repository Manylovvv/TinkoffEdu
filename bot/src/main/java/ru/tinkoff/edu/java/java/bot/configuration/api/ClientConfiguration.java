package ru.tinkoff.edu.java.java.bot.configuration.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient();
    }
}
