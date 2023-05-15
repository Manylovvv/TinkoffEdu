package ru.tinkoff.edu.java.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;

@Configuration
public class ClientConfig {
    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient();
    }
}
