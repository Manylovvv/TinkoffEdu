package ru.tinkoff.edu.java.scrapper.configuration.access;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgService;
import ru.tinkoff.edu.java.scrapper.service.refactor.Refactor;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;

/**Аннотация, которая определяет класс конфигурационным и содержит бины*/
@Configuration
/**
 * Аннотация, которая определяет, что этот класс будет создан только
 * при наличии свойства с access-type со значением Jdbc
 */
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    /**
     * Аннотация для методов в конфигурационном файле Спринга.
     * Она указывает на то, что метод должен быть оберткой над объектом-бином Спринга
     */
    @Bean
    public LinkService linkService(
        ChatLinkRepository chatLinkRepository, LinkRepository linkRepository,
        Refactor refactor, LinkRenew linkRenew) {
        return new JdbcLinkService(chatLinkRepository, linkRepository, refactor, linkRenew);
    }

    /**
     * объявление бина TgChatService и принятие в качестве параметра ChatLinkRepository
     */
    @Bean
    public TgChatService tgChatService(ChatLinkRepository repository) {
        return new JdbcTgService(repository);
    }
}
