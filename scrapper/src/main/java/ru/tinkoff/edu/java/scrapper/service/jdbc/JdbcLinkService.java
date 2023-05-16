package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.converter.Converter;

import ru.tinkoff.edu.java.scrapper.service.Updater.LinkManipulator;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;

import java.net.URI;
import java.util.List;
@Primary
@AllArgsConstructor
@Service
public class JdbcLinkService implements LinkService {
    private final ChatLinkRepository repository;
    private final LinkRepository linkRepository;
    private final Converter converter;
    private final LinkManipulator linkManipulator;

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        return converter.linkToLinkResponse(repository.trackLink(tgChatId, linkManipulator.createLink(url)));
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        return converter.linkToLinkResponse(repository.untrackLink(tgChatId, url));
    }

    @Override
    public ListLinksResponse listAll(Long tgChatId) {
        return converter.linksToListLinksResponse(repository.getAllLinks(tgChatId));
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
