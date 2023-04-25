package ru.tinkoff.edu.java.scrapper.configuration.access;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.ChatLinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.LinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.TgChatEntityRepository;
import ru.tinkoff.edu.java.scrapper.service.Updater.LinkManipulator;
import ru.tinkoff.edu.java.scrapper.service.converter.Converter;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;


@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {
    @Bean
    public LinkService linkService(
            TgChatEntityRepository tgChatEntityRepository,
            LinkEntityRepository linkEntityRepository,
            ChatLinkEntityRepository chatLinkEntityRepository,
            LinkManipulator linkManipulator,
            Converter converter
    ) {
        return new JpaLinkService(tgChatEntityRepository, linkEntityRepository, chatLinkEntityRepository, linkManipulator, converter);
    }

    @Bean
    public TgChatService tgChatService(
            TgChatEntityRepository tgChatEntityRepository,
            LinkEntityRepository linkEntityRepository,
            ChatLinkEntityRepository chatLinkEntityRepository
    ) {
        return new JpaTgChatService(tgChatEntityRepository, linkEntityRepository, chatLinkEntityRepository);
    }
}
