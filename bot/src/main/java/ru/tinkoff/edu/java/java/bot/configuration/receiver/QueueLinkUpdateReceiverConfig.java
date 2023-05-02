package ru.tinkoff.edu.java.java.bot.configuration.receiver;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.model.core.BotCreator;
import ru.tinkoff.edu.java.java.bot.service.LinkUpdateReceiver;
import ru.tinkoff.edu.java.java.bot.service.QueueLinkUpdateReceiver;


@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class QueueLinkUpdateReceiverConfig {
    @Bean
    public LinkUpdateReceiver linkUpdateReceiver(
            BotCreator bot
    ) {
        return new QueueLinkUpdateReceiver(bot);
    }
}
