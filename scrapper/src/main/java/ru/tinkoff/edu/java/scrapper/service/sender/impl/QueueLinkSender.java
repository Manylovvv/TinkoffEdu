package ru.tinkoff.edu.java.scrapper.service.sender.impl;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.tinkoff.edu.java.scrapper.controller.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.sender.interfaces.LinkUpdateSender;

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
public class QueueLinkSender implements LinkUpdateSender {
    private final LinkService linkService;
    private final Queue queue;
    private final RabbitTemplate template;

    /**
     * Метод sendUpdate создает объект LinkUpdate с идентификатором,
     * URL-адресом, описанием и идентификаторами чата ссылки,
     * а затем отправляет его в очередь с помощью RabbitTemplate.
     */
    @Override
    public void sendUpdate(Link link, String description) {
        LinkUpdate request = new LinkUpdate();
        request.setId(link.getId());
        request.setUrl(link.getLink());
        request.setDescription(description);
        request.setTgChatIds(linkService.getChatsForLink(link).stream().map(TgChat::getTgChatId).toList());
        template.convertAndSend(queue.getName(), request);
    }
}
