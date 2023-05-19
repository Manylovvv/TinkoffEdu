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

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final ChatLinkRepository repository;
    private final LinkRepository linkRepository;
    private final Refactor refactor;
    private final LinkRenew linkRenew;

    /**метод принимает идентификатор чата tg и объект URI, представляющий ссылку, и возвращает объект LinkResponse*/
    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        return refactor.linkToLinkResponse(repository.trackLink(tgChatId, linkRenew.createLink(url)));
    }

    /**метод принимает идентификатор чата tg и объект URI, представляющий ссылку, и возвращает объект LinkResponse.*/
    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        return refactor.linkToLinkResponse(repository.untrackLink(tgChatId, url));
    }

    /**метод принимает идентификатор чата tg и возвращает объект ListLinksResponse.*/
    @Override
    public ListLinksResponse listAll(Long tgChatId) {
        return refactor.linksToListLinksResponse(repository.getAllLinks(tgChatId));
    }

    /**метод возвращает список объектов Link*/
    @Override
    public List<Link> findLinksForUpdate() {
        return linkRepository.findAllForUpdate();
    }

    /**метод принимает объект Link и возвращает список объектов TgChat*/
    /**Он использует объект ChatLinkRepository для получения всех чатов Telegram, которые отслеживают данную ссылку*/
    @Override
    public List<TgChat> getChatsForLink(Link link) {
        return repository.getChatsForLink(link);
    }

    /**метод принимает объект Link и обновляет его информацию с помощью объекта LinkRepository*/
    @Override
    public void updateLink(Link link) {
        linkRepository.update(link);
    }
}
