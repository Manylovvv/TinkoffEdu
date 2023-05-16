package ru.tinkoff.edu.java.scrapper.service.interfaces;

import java.net.URI;
import java.util.List;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;

public interface LinkService {
    void updateLink(Link link);

    LinkResponse remove(Long tgChatId, URI url);

    LinkResponse add(Long tgChatId, URI url);

    List<Link> findLinksForUpdate();

    ListLinksResponse listAll(Long tgChatId);

    List<TgChat> getChatsForLink(Link link);

}
