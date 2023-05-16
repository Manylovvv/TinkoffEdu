package ru.tinkoff.edu.java.scrapper.configuration.access;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.Updater.LinkManipulator;
import ru.tinkoff.edu.java.scrapper.service.converter.Converter;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;


@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    @Bean
    public LinkService linkService(
            ChatLinkRepository chatLinkRepository,
            LinkRepository linkRepository,
            Converter converter,
            LinkManipulator linkManipulator
    ) {
        return new JdbcLinkService(chatLinkRepository, linkRepository, converter, linkManipulator);
    }

    @Bean
    public TgChatService tgChatService(
            ChatLinkRepository repository
    ) {
        return new JdbcTgChatService(repository);
    }
}
