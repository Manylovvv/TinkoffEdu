package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import ru.tinkoff.edu.java.scrapper.domain.jooq.Tables;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.exception.ResourceNotFoundException;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;

import java.util.List;

@AllArgsConstructor
@Service
public class JooqTgChatService implements TgChatService {
    private DSLContext context;

    @Transactional
    @Override
    public void register(Long tgChatId) {
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat != null) {
            throw new ResourceNotFoundException("Tg chat '" + tgChatId + "' already exists");
        }
        context.insertInto(Tables.CHAT, Tables.CHAT.TG_CHAT_ID).values(tgChatId).execute();
    }

    @Transactional
    @Override
    public void unregister(Long tgChatId) {
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat == null) {
            throw new ResourceNotFoundException("Tg chat '" + tgChatId + "' was not found");
        }
        List<Link> trackedLinks = context.select(Tables.LINK.fields()).from(Tables.CHAT_LINK)
                .join(Tables.CHAT).on(Tables.CHAT.ID.eq(Tables.CHAT_LINK.CHAT_ID))
                .join(Tables.LINK).on(Tables.LINK.ID.eq(Tables.CHAT_LINK.LINK_ID))
                .where(Tables.CHAT.ID.eq(tgChat.getId())).fetchInto(Link.class);
        for (Link link: trackedLinks) {
            context.deleteFrom(Tables.CHAT_LINK).where(Tables.CHAT_LINK.LINK_ID.eq(link.getId()))
                    .and(Tables.CHAT_LINK.CHAT_ID.eq(tgChat.getId())).execute();
            int count = context.selectCount().from(Tables.CHAT_LINK)
                    .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId()).and(Tables.CHAT_LINK.CHAT_ID.notEqual(tgChat.getId()))).fetchOne(0, int.class);
            if (count == 0) {
                context.deleteFrom(Tables.LINK).where(Tables.LINK.ID.eq(link.getId())).execute();
            }
        }
        context.deleteFrom(Tables.CHAT).where(Tables.CHAT.TG_CHAT_ID.eq(tgChatId)).execute();
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
