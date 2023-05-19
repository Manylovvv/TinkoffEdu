package ru.tinkoff.edu.java.java.bot.configuration.receiver;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.service.AbstractUpdateReceiver;
import ru.tinkoff.edu.java.java.bot.service.HttpUpdateReceiver;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;

/**
 * Аннотация указывает, что класс содержит методы определения @Bean
 * класс следует загружать только в том случае, если установлено свойство app.use-queue=false
 */
@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpUpdateReceiverConfiguration {
    /**
     * метод, который создает новый экземпляр подкласса AbstractUpdateReceiver.
     * создает новый объект HttpUpdateReceiver.
     * @param bot
     * @return - HttpUpdateReceiver
     */
    @Bean
    public AbstractUpdateReceiver linkUpdateReceiver(
        BotCreator bot
    ) {
        return new HttpUpdateReceiver(bot);
    }
}
