package ru.tinkoff.edu.java.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.model.core.BotCreator;
import ru.tinkoff.edu.java.bot.model.command.*;
import ru.tinkoff.edu.java.bot.scrapper.api.ScrapperClient;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
        public static void main(String[] args) {
                var ctx = SpringApplication.run(BotApplication.class, args);
                ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
                System.out.println(config);
                ScrapperClient client = ctx.getBean(ScrapperClient.class);
                BotCreator TgBot = new BotCreator(
                        config.bot().token(),
                        new StartCommand(client),
                        new ListCommand(client),
                        new TrackCommand(client),
                        new UntrackCommand(client),
                        new HelpCommand());


        }
}