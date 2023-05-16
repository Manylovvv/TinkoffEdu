package ru.tinkoff.edu.java.java.bot.configuration.receiver;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.service.AbstractUpdateReceiver;
import ru.tinkoff.edu.java.java.bot.service.QueueUpdateReceiver;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class QueueUpdateReceiverConfiguration {
    @Bean
    public AbstractUpdateReceiver linkUpdateReceiver(
        BotCreator bot
    ) {
        return new QueueUpdateReceiver(bot);
    }
}

