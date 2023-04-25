package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import ru.tinkoff.edu.java.scrapper.domain.jooq.Tables;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.converter.Converter;
import ru.tinkoff.edu.java.scrapper.exception.ResourceNotFoundException;

import ru.tinkoff.edu.java.scrapper.service.Updater.LinkManipulator;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class JooqLinkService implements LinkService {
    private DSLContext context;
    private final Converter converter;
    private final LinkManipulator linkManipulator;

    @Transactional
    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat == null) {
            throw new ResourceNotFoundException("Tg chat '" + tgChatId + "' was not found");
        }
        Link link = getLink(url);
        if (link == null) {
            Link newLink = linkManipulator.createLink(url);
            context.insertInto(Tables.LINK, Tables.LINK.LINK_, Tables.LINK.LAST_UPDATE, Tables.LINK.LAST_ACTIVITY, Tables.LINK.ANSWER_COUNT,
                    Tables.LINK.OPEN_ISSUES_COUNT).values(newLink.getLink().toString(), newLink.getLastUpdate().toLocalDateTime(),
                    newLink.getLastActivity().toLocalDateTime(), newLink.getAnswerCount(), newLink.getOpenIssuesCount()).execute();
            link = getLink(url);
            if (link == null) {
                throw new RuntimeException("Error while adding url '" + url + "'");
            }
        }
        else {
            int count = context.selectCount().from(Tables.CHAT_LINK)
                    .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId()).and(Tables.CHAT_LINK.CHAT_ID.eq(tgChat.getId()))).fetchOne(0, int.class);
            if (count != 0) {
                throw new RuntimeException("Link '" + url + "' is already tracking by tg chat '" + tgChatId + "'");
            }
        }
        context.insertInto(Tables.CHAT_LINK, Tables.CHAT_LINK.CHAT_ID, Tables.CHAT_LINK.LINK_ID)
                .values(tgChat.getId(), link.getId()).execute();
        return converter.linkToLinkResponse(link);
    }

    @Transactional
    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        Link link = getLink(url);
        if (link == null) {
            throw new ResourceNotFoundException("Link '" + url + "' was not found");
        }
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat == null) {
            throw new ResourceNotFoundException("Tg chat '" + tgChatId + "' was not found");
        }
        context.deleteFrom(Tables.CHAT_LINK)
                .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId())).and(Tables.CHAT_LINK.CHAT_ID.eq(tgChat.getId())).execute();
        int count = context.selectCount().from(Tables.CHAT_LINK)
                .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId()).and(Tables.CHAT_LINK.CHAT_ID.notEqual(tgChat.getId()))).fetchOne(0, int.class);
        if (count == 0) {
            context.deleteFrom(Tables.LINK).where(Tables.LINK.ID.eq(link.getId())).execute();
        }
        return converter.linkToLinkResponse(link);
    }

    @Transactional
    @Override
    public ListLinksResponse listAll(Long tgChatId) {
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat == null) {
            throw new ResourceNotFoundException("Tg chat '" + tgChatId + "' was not found");
        }
        return converter.linksToListLinksResponse(context.select(Tables.LINK.fields()).from(Tables.LINK)
                .join(Tables.CHAT_LINK).on(Tables.CHAT_LINK.LINK_ID.eq(Tables.LINK.ID))
                .where(Tables.CHAT_LINK.CHAT_ID.eq(tgChat.getId())).fetchInto(Link.class));
    }

    @Override
    public List<Link> findLinksForUpdate() {
        return context.select(Tables.LINK.fields()).from(Tables.LINK)
                .where(Tables.LINK.LAST_UPDATE.lessThan(LocalDateTime.now().minusMinutes(5))).fetchInto(Link.class);
    }

    @Override
    public List<TgChat> getChatsForLink(Link link) {
        return context.select(Tables.CHAT.fields()).from(Tables.CHAT_LINK).join(Tables.CHAT).on(Tables.CHAT.ID.eq(Tables.CHAT_LINK.CHAT_ID))
                .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId())).fetchInto(TgChat.class);
    }

    @Override
    public void updateLink(Link link) {
        context.update(Tables.LINK).set(Tables.LINK.LAST_UPDATE, link.getLastUpdate().toLocalDateTime())
                .set(Tables.LINK.LAST_ACTIVITY, link.getLastActivity().toLocalDateTime())
                .set(Tables.LINK.ANSWER_COUNT, link.getAnswerCount())
                .set(Tables.LINK.OPEN_ISSUES_COUNT, link.getOpenIssuesCount())
                .where(Tables.LINK.ID.eq(link.getId())).execute();
    }

    private Link getLink(URI url) {
        List<Link> links = context.select(Tables.LINK.fields()).from(Tables.LINK)
                .where(Tables.LINK.LINK_.eq(url.toString())).fetchInto(Link.class);
        if (links.size() == 0) {
            return null;
        }
        return links.get(0);
    }

    private TgChat getTgChat(Long tgChatId) {
        List<TgChat> chats = context.select(Tables.CHAT.fields()).from(Tables.CHAT)
                .where(Tables.CHAT.TG_CHAT_ID.eq(tgChatId)).fetchInto(TgChat.class);
        if (chats.size() == 0) {
            return null;
        }
        return chats.get(0);
    }
}
