package ru.tinkoff.edu.java.scrapper.configuration.access;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.ChatLinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.LinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.TgChatEntityRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;
import ru.tinkoff.edu.java.scrapper.service.refactor.Refactor;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService linkService(
        ApplicationConfig config, LinkEntityRepository linkEntityRepository, LinkRenew linkRenew, Refactor refactor,
        TgChatEntityRepository tgChatEntityRepository, ChatLinkEntityRepository chatLinkEntityRepository) {
        return new JpaLinkService(config, linkEntityRepository, linkRenew, refactor,
            tgChatEntityRepository, chatLinkEntityRepository);
    }

    @Bean
    public TgChatService tgChatService(TgChatEntityRepository tgChatEntityRepository,
        LinkEntityRepository linkEntityRepository,
        ChatLinkEntityRepository chatLinkEntityRepository) {
        return new JpaTgChatService(tgChatEntityRepository, linkEntityRepository, chatLinkEntityRepository);
    }
}
