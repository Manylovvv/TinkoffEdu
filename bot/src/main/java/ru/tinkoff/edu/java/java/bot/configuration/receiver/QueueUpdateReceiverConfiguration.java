package ru.tinkoff.edu.java.java.bot.configuration.receiver;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.service.AbstractUpdateReceiver;
import ru.tinkoff.edu.java.java.bot.service.QueueUpdateReceiver;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;

/**
 * Аннотация указывает, что класс содержит методы определения @Bean
 * класс следует загружать только в том случае, если установлено свойство app.use-queue=true
 * Класс QueueUpdateReceiver является подклассом AbstractUpdateReceiver,
 * который получает обновления из очереди RabbitMQ.
 */
@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class QueueUpdateReceiverConfiguration {
    /**
     * Если для use-queue задано значение true, то метод linkUpdateReceiver в
     * QueueUpdateReceiverConfiguration создает новый объект QueueUpdateReceiver
     * @param bot
     * @return - QueueUpdateReceiver
     */
    @Bean
    public AbstractUpdateReceiver linkUpdateReceiver(
        BotCreator bot
    ) {
        return new QueueUpdateReceiver(bot);
    }
}

