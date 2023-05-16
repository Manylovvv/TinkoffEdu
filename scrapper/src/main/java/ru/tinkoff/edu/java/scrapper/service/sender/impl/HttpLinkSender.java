package ru.tinkoff.edu.java.scrapper.service.sender.impl;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.api.BotClient;
import ru.tinkoff.edu.java.scrapper.controller.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.sender.interfaces.LinkUpdateSender;

@AllArgsConstructor
public class HttpLinkSender implements LinkUpdateSender {
    private final BotClient botClient;
    private final LinkService linkService;

    @Override
    public void sendUpdate(Link link, String description) {
        LinkUpdate request = new LinkUpdate();
        request.setId(link.getId());
        request.setUrl(link.getLink());
        request.setDescription(description);
        request.setTgChatIds(linkService.getChatsForLink(link).stream().map(TgChat::getTgChatId).toList());
        botClient.sendUpdate(request);
    }
}
