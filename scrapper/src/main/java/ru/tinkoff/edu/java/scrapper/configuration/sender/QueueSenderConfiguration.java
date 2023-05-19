package ru.tinkoff.edu.java.scrapper.configuration.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.sender.impl.QueueLinkSender;
import ru.tinkoff.edu.java.scrapper.service.sender.interfaces.LinkUpdateSender;

/**Аннотация, которая определяет класс конфигурационным и содержит бины*/
@Configuration
/**
 * Аннотация, которая указывает, что данный класс будет создан только в том случае,
 * если в файле application.properties установлено свойство app.use-queue равное true
 */
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class QueueSenderConfiguration {
    @Bean
    public LinkUpdateSender linkUpdateSender(
            LinkService linkService,
            Queue queue,
            RabbitTemplate rabbitTemplate
    ) {
        return new QueueLinkSender(linkService, queue, rabbitTemplate);
    }
}
