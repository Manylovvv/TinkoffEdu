package ru.tinkoff.edu.java.scrapper.service.jdbc;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.refactor.Refactor;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;

@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final ChatLinkRepository repository;
    private final LinkRepository linkRepository;
    private final Refactor refactor;
    private final LinkRenew linkRenew;

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        return refactor.linkToLinkResponse(repository.trackLink(tgChatId, linkRenew.createLink(url)));
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        return refactor.linkToLinkResponse(repository.untrackLink(tgChatId, url));
    }

    @Override
    public ListLinksResponse listAll(Long tgChatId) {
        return refactor.linksToListLinksResponse(repository.getAllLinks(tgChatId));
    }

    @Override
    public List<Link> findLinksForUpdate() {
        return linkRepository.findAllForUpdate();
    }

    @Override
    public List<TgChat> getChatsForLink(Link link) {
        return repository.getChatsForLink(link);
    }

    @Override
    public void updateLink(Link link) {
        linkRepository.update(link);
    }
}
