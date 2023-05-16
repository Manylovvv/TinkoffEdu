package ru.tinkoff.edu.java.scrapper.service.jpa;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.ChatLinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.LinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.TgChatEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.ChatLinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.TgChatEntity;
import ru.tinkoff.edu.java.scrapper.excontroller.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.refactor.Refactor;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;

@AllArgsConstructor
public class JpaLinkService implements LinkService {
    private final ApplicationConfig config;
    private final LinkEntityRepository linkEntityRepository;
    private final LinkRenew linkRenew;
    private final Refactor refactor;
    private final TgChatEntityRepository tgChatEntityRepository;
    private final ChatLinkEntityRepository chatLinkEntityRepository;

    @Transactional
    @Override
    public List<TgChat> getChatsForLink(Link link) {
        return chatLinkEntityRepository.getTgChatsByLinkId(link.getId()).stream().map(refactor::tgChatEntityToTgChat)
            .toList();
    }

    @Transactional
    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        TgChatEntity tgChatEntity = tgChatEntityRepository.findByTgChatId(tgChatId)
            .orElseThrow(() -> new NotFoundException("Чат '" + tgChatId + "' не найден"));
        LinkEntity linkEntity = linkEntityRepository.findByLink(url.toString())
            .orElseThrow(() -> new NotFoundException("Ссылка '" + url + "' не найдена"));
        chatLinkEntityRepository.deleteByTgChatAndLink(tgChatEntity, linkEntity);
        if (chatLinkEntityRepository.getTgChatsByLinkId(linkEntity.getId()).size() == 0) {
            linkEntityRepository.deleteById(linkEntity.getId());
        }
        try {
            return refactor.linkEntityToLinkResponse(linkEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        TgChatEntity tgChatEntity = tgChatEntityRepository.findByTgChatId(tgChatId)
            .orElseThrow(() -> new NotFoundException("Чат '" + tgChatId + "' не найден"));
        LinkEntity linkEntity = linkEntityRepository.findByLink(url.toString()).orElseGet(
            () -> linkEntityRepository.save(linkRenew.createLinkEntity(url)));
        if (chatLinkEntityRepository.findByTgChatAndLink(tgChatEntity, linkEntity).isPresent()) {
            throw new RuntimeException("Ссылка '" + url + "' уже отслеживается чатом '" + tgChatId + "'");
        }
        ChatLinkEntity chatLinkEntity = new ChatLinkEntity();
        chatLinkEntity.setTgChat(tgChatEntity);
        chatLinkEntity.setLink(linkEntity);
        chatLinkEntityRepository.save(chatLinkEntity);
        try {
            return refactor.linkEntityToLinkResponse(linkEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public ListLinksResponse listAll(Long tgChatId) {
        if (tgChatEntityRepository.findByTgChatId(tgChatId).isEmpty()) {
            throw new NotFoundException("Чат '" + tgChatId + "' не найден");
        }
        return refactor.linkEntitiesToListLinksResponse(chatLinkEntityRepository.getLinksByTgChatId(tgChatId));
    }

    @Transactional
    @Override
    public List<Link> findLinksForUpdate() {
        return linkEntityRepository.findAll().stream().filter((LinkEntity le) ->
            le.getLastUpdate().isBefore(OffsetDateTime.of(
                LocalDateTime.now().minusMinutes(config.getUpdateInterval()),
                ZoneOffset.UTC
            ))
        ).map((LinkEntity le) -> {
            try {
                return refactor.linkEntityToLink(le);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Transactional
    @Override
    public void updateLink(Link link) {
        LinkEntity linkEntity = linkEntityRepository.findById(link.getId())
            .orElseThrow(() -> new NotFoundException("Ссылка '" + link.getLink() + "' не найдена"));
        linkEntity.setAnswerCount(link.getAnswerCount());
        linkEntity.setLastUpdate(link.getLastUpdate());
        linkEntity.setLastActivity(link.getLastActivity());
        linkEntity.setOpenIssuesCount(link.getOpenIssuesCount());
        linkEntityRepository.save(linkEntity);
    }
}
