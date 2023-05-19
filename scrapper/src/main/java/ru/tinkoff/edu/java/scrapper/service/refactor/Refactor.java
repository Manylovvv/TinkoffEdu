package ru.tinkoff.edu.java.scrapper.service.refactor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.TgChatEntity;

/**
 * Аннотация применяется при указании класса в качестве Spring-компонента.
 * */
@Component
public class Refactor {
    /**
     * Принимает список объектов LinkEntity и возвращает объект ListLinksResponse.
     * Он использует метод map для преобразования каждого объекта LinkEntity
     * в объект LinkResponse путем вызова метода linkEntityToLinkResponse,
     * а затем добавляет все преобразованные объекты в новый список.
     * Наконец, он возвращает новый объект ListLinksResponse с
     * преобразованным списком и исходным размером входного списка.
     */
    public ListLinksResponse linkEntitiesToListLinksResponse(List<LinkEntity> links) {
        return new ListLinksResponse(links.stream().map((LinkEntity le) -> {
            try {
                return linkEntityToLinkResponse(le);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).toList(), links.size());
    }

    /**
     * Принимает объект TgChatEntity и возвращает объект TgChat.
     * Он просто создает новый объект TgChat, используя свойства id и tgChatId входного объекта
     */
    public TgChat tgChatEntityToTgChat(TgChatEntity tgChatEntity) {
        return new TgChat(tgChatEntity.getId(), tgChatEntity.getTgChatId());
    }

    /**
     * принимает объект Link и возвращает объект LinkResponse.
     * Он создает новый объект LinkResponse, используя свойства id и link входного объекта.
     */
    public LinkResponse linkToLinkResponse(Link link) {
        return new LinkResponse(link.getId(), link.getLink());
    }

    /**
     * Принимает список объектов Link и возвращает объект ListLinksResponse.
     * Он использует метод карты для преобразования каждого объекта Link
     * в объект LinkResponse путем вызова метода linkToLinkResponse, а затем
     * добавляет все преобразованные объекты в новый список.
     * Наконец, он возвращает новый объект ListLinksResponse с преобразованным
     * списком и исходным размером входного списка
     */
    public ListLinksResponse linksToListLinksResponse(List<Link> links) {
        return new ListLinksResponse(links.stream().map(this::linkToLinkResponse).toList(), links.size());
    }

    /**
     * принимает объект LinkEntity и возвращает объект Link
     * Он создает новый объект Link, используя свойства id, link, lastUpdate,
     * lastActivity, openIssuesCount и answerCount входного объекта.
     */
    public Link linkEntityToLink(LinkEntity linkEntity) throws URISyntaxException {
        return new Link(linkEntity.getId(),
            new URI(linkEntity.getLink()),
            linkEntity.getLastUpdate(),
            linkEntity.getLastActivity(),
            linkEntity.getOpenIssuesCount(),
            linkEntity.getAnswerCount()
        );
    }

    /**
     * принимает объект LinkEntity и возвращает объект LinkResponse. Он создает новый объект LinkResponse,
     * используя свойства id и ссылки входного объекта, создавая новый объект URI из свойства ссылки
     */
    public LinkResponse linkEntityToLinkResponse(LinkEntity linkEntity) throws URISyntaxException {
        return new LinkResponse(linkEntity.getId(), new URI(linkEntity.getLink()));
    }
}
