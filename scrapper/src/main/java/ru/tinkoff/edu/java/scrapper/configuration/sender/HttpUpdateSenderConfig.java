package ru.tinkoff.edu.java.scrapper.configuration.sender;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.api.BotClient;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.sender.HttpLinkUpdateSender;
import ru.tinkoff.edu.java.scrapper.service.sender.LinkUpdateSender;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpUpdateSenderConfig {
    @Bean
    public LinkUpdateSender linkUpdateSender(
            BotClient botClient,
            LinkService linkService
    ) {
        return new HttpLinkUpdateSender(botClient, linkService);
    }
}
