package ru.tinkoff.edu.java.java.bot.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.model.command.*;
import ru.tinkoff.edu.java.java.bot.model.core.BotCreator;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;


@Configuration
public class BotConfig {
    @Bean
    public BotCreator bot(
            ApplicationConfig config,
            ScrapperClient client
    ){
        return new BotCreator(
                config.token(),
                new StartCommand(client),
                new ListCommand(client),
                new TrackCommand(client),
                new UntrackCommand(client),
                new HelpCommand());
    }
}