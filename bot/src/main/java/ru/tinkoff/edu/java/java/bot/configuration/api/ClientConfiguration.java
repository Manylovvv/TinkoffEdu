package ru.tinkoff.edu.java.java.bot.configuration.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;

/**
 * Аннотация указывает, что класс содержит методы определения @Bean
 */
@Configuration
public class ClientConfiguration {
    /**
     * определяет bean-компонент типа ScrapperClient
     * @return новый экземпляр ScrapperClient при запросе компонента
     */
    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient();
    }
}
