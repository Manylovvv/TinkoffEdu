package ru.tinkoff.edu.java.scrapper.configuration.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.api.BotClient;
import ru.tinkoff.edu.java.scrapper.api.GitHubClient;
import ru.tinkoff.edu.java.scrapper.api.StackOverflowClient;

/**Аннотация, которая определяет класс конфигурационным и содержит бины*/
@Configuration
public class ClientConfiguration {

    /**
     * Аннотация для методов в конфигурационном файле Спринга.
     * Она указывает на то, что метод должен быть оберткой над объектом-бином Спринга
     * Аннотация, указывающая создание бина gitHubClient
     */
    @Bean("gitHubClient")
    public GitHubClient gitHubClient() {
        return new GitHubClient();
    }

    /**
     * Аннотация, указывающая создание бина stackOverflowClient
     */
    @Bean("stackOverflowClient")
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient();
    }

    /**
     * Аннотация, указывающая создание бина botClient
     */
    @Bean("botClient")
    public BotClient botClient() {
        return new BotClient();
    }
}
