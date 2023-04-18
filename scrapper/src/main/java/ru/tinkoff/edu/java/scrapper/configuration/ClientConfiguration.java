package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.api.BotClient;
import ru.tinkoff.edu.java.scrapper.api.GitHubClient;
import ru.tinkoff.edu.java.scrapper.api.StackOverflowClient;

@Configuration
public class ClientConfiguration {

    @Bean("gitHubClient")
    public GitHubClient gitHubClient() {
        return new GitHubClient();
    }

    @Bean("stackOverflowClient")
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient();
    }

    @Bean("botClient")
    public BotClient botClient() {
        return new BotClient();
    }
}
