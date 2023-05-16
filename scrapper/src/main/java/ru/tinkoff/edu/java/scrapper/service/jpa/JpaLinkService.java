package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.ChatLinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.LinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.TgChatEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.ChatLinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.TgChatEntity;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.exception.ResourceNotFoundException;
import ru.tinkoff.edu.java.scrapper.service.Updater.LinkManipulator;
import ru.tinkoff.edu.java.scrapper.service.converter.Converter;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@AllArgsConstructor
public class JpaLinkService implements LinkService {
    private final TgChatEntityRepository tgChatEntityRepository;
    private final LinkEntityRepository linkEntityRepository;
    private final ChatLinkEntityRepository chatLinkEntityRepository;
    private final LinkManipulator linkManipulator;
    private final Converter converter;

    @Transactional
    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        TgChatEntity tgChatEntity = tgChatEntityRepository.findByTgChatId(tgChatId)
                .orElseThrow(() -> new ResourceNotFoundException("Tg chat '" + tgChatId + "' was not found"));
        LinkEntity linkEntity = linkEntityRepository.findByLink(url.toString()).orElseGet(
                () -> linkEntityRepository.save(linkManipulator.createLinkEntity(url)));
        if (chatLinkEntityRepository.findByTgChatAndLink(tgChatEntity, linkEntity).isPresent()) {
            throw new RuntimeException("Link '" + url + "' is already tracking by tg chat '" + tgChatId + "'");
        }
        ChatLinkEntity chatLinkEntity = new ChatLinkEntity();
        chatLinkEntity.setTgChat(tgChatEntity);
        chatLinkEntity.setLink(linkEntity);
        chatLinkEntityRepository.save(chatLinkEntity);
        try {
            return converter.linkEntityToLinkResponse(linkEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        TgChatEntity tgChatEntity = tgChatEntityRepository.findByTgChatId(tgChatId)
                .orElseThrow(() -> new ResourceNotFoundException("Tg chat '" + tgChatId + "' was not found"));
        LinkEntity linkEntity = linkEntityRepository.findByLink(url.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Link '" + url + "' was not found"));
        chatLinkEntityRepository.deleteByTgChatAndLink(tgChatEntity, linkEntity);
        if (chatLinkEntityRepository.getTgChatsByLinkId(linkEntity.getId()).size() == 0) {
            linkEntityRepository.deleteById(linkEntity.getId());
        }
        try {
            return converter.linkEntityToLinkResponse(linkEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public ListLinksResponse listAll(Long tgChatId) {
        if (tgChatEntityRepository.findByTgChatId(tgChatId).isEmpty()) {
            throw new ResourceNotFoundException("Tg chat '" + tgChatId + "' was not found");
        }
        return converter.linkEntitiesToListLinksResponse(chatLinkEntityRepository.getLinksByTgChatId(tgChatId));
    }

    @Transactional
    @Override
    public List<Link> findLinksForUpdate() {
        return linkEntityRepository.findAll().stream().filter((LinkEntity le) ->
            le.getLastUpdate().isBefore(OffsetDateTime.of(LocalDateTime.now().minusMinutes(5), ZoneOffset.UTC))
        ).map((LinkEntity le) -> {
            try {
                return converter.linkEntityToLink(le);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Transactional
    @Override
    public List<TgChat> getChatsForLink(Link link) {
        return chatLinkEntityRepository.getTgChatsByLinkId(link.getId()).stream().map(converter::tgChatEntityToTgChat).toList();
    }

    @Transactional
    @Override
    public void updateLink(Link link) {
        LinkEntity linkEntity = linkEntityRepository.findById(link.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Link '" + link.getLink() + "' was not found"));
        linkEntity.setAnswerCount(link.getAnswerCount());
        linkEntity.setLastUpdate(link.getLastUpdate());
        linkEntity.setLastActivity(link.getLastActivity());
        linkEntity.setOpenIssuesCount(link.getOpenIssuesCount());
        linkEntityRepository.save(linkEntity);
    }
}
