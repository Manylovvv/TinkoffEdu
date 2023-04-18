package ru.tinkoff.edu.java.scrapper.service.interfaces;

import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;


import java.net.URI;
import java.util.List;

public interface LinkService {
    LinkResponse add(Long tgChatId, URI url);
    LinkResponse remove(Long tgChatId, URI url);
    ListLinksResponse listAll(Long tgChatId);
    List<Link> findLinksForUpdate();
    List<TgChat> getChatsForLink(Link link);
    void updateLink(Link link);
}
