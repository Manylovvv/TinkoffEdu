package ru.tinkoff.edu.java.scrapper.configuration.access;

import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.service.Updater.LinkManipulator;
import ru.tinkoff.edu.java.scrapper.service.converter.Converter;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqTgChatService;


@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfig {
    @Bean
    public LinkService linkService(
            DSLContext context,
            Converter converter,
            LinkManipulator linkManipulator,
            ApplicationConfig config
    ) {
        return new JooqLinkService(context, converter, linkManipulator, config);
    }

    @Bean
    public TgChatService tgChatService(
            DSLContext context
    ) {
        return new JooqTgChatService(context);
    }
}
