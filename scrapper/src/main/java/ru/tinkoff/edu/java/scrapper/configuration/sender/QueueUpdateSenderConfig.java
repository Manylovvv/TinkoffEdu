package ru.tinkoff.edu.java.scrapper.configuration.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.sender.LinkUpdateSender;
import ru.tinkoff.edu.java.scrapper.service.sender.QueueLinkUpdateSender;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class QueueUpdateSenderConfig {
    @Bean
    public LinkUpdateSender linkUpdateSender(
            LinkService linkService,
            RabbitTemplate template,
            Queue queue
    ) {
        return new QueueLinkUpdateSender(linkService, template, queue);
    }
}
