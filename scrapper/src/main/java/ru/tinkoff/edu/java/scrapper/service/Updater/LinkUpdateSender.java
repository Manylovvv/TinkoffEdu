package ru.tinkoff.edu.java.scrapper.service.Updater;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.api.BotClient;

@AllArgsConstructor
@Service
public class LinkUpdateSender {
    private final BotClient botClient;
    private final LinkService linkService;

    public void sendUpdate(Link link, String description) {
        LinkUpdate request = new LinkUpdate();
        request.setId(link.getId());
        request.setUrl(link.getLink());
        request.setDescription(description);
        request.setTgChatIds(linkService.getChatsForLink(link).stream().map(TgChat::getTgChatId).toList());
        botClient.sendUpdate(request);
    }
}
