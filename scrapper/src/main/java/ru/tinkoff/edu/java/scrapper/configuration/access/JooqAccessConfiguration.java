package ru.tinkoff.edu.java.scrapper.configuration.access;

import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqTgService;
import ru.tinkoff.edu.java.scrapper.service.refactor.Refactor;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;

/**Аннотация, которая определяет класс конфигурационным и содержит бины*/
@Configuration
/**
 * Аннотация, которая определяет, что этот класс будет создан
 * только при наличии свойства с access-type со значением jooq
 */
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    /**
     *  Аннотация для методов в конфигурационном файле Спринга.
     *  Она указывает на то, что метод должен быть оберткой над объектом-бином Спринга
     */
    @Bean
    public LinkService linkService(DSLContext context, Refactor refactor,
        LinkRenew linkRenew, ApplicationConfig config) {
        return new JooqLinkService(context, refactor, linkRenew, config);
    }

    @Bean
    /**DSLContext объект доступа к бд*/
    public TgChatService tgChatService(DSLContext context) {
        return new JooqTgService(context);
    }
}
