package ru.tinkoff.edu.java.java.bot.configuration.receiver;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.java.bot.model.core.BotCreator;
import ru.tinkoff.edu.java.java.bot.service.AbstractLinkUpdateReceiver;
import ru.tinkoff.edu.java.java.bot.service.HttpLinkUpdateReceiver;




@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpLinkUpdateReceiverConfig {
    @Bean
    public AbstractLinkUpdateReceiver linkUpdateReceiver(
        BotCreator bot
    ) {
        return new HttpLinkUpdateReceiver(bot);
    }
}
