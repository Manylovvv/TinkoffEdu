package ru.tinkoff.edu.java.java.bot.configuration.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.HelpCommand;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.ListCommand;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.StartCommand;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.TrackCommand;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.UntrackCommand;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;
import ru.tinkoff.edu.java.java.bot.configuration.ApplicationConfig;

@Configuration
public class BotConfiguration {
    @Bean
    public BotCreator bot(
        ScrapperClient client,
        ApplicationConfig config
    ) {
        return new BotCreator(
            config.token(), new StartCommand(client),
            new ListCommand(client), new TrackCommand(client),
            new UntrackCommand(client), new HelpCommand());
    }
}
