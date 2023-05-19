package ru.tinkoff.edu.java.scrapper.service.jooq;

import java.util.List;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.jooq.Tables;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.excontroller.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
public class JooqTgService implements TgChatService {
    private DSLContext context;

    @Transactional
    /**Метод register вставляет новый объект TgChat с заданным идентификатором, если он еще не существует*/
    @Override
    public void register(Long tgChatId) {
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat != null) {
            throw new NotFoundException("Чат '" + tgChatId + "' уже существует");
        }
        context.insertInto(Tables.CHAT, Tables.CHAT.TG_CHAT_ID).values(tgChatId).execute();
    }

    /**
     * Метод unregister находит TgChat с заданным идентификатором, удаляет все объекты ChatLink,
     * которые связывают чат с любыми ссылками, удаляет объект Link, если он больше не отслеживается
     * ни одним чатом, и, наконец, удаляет объект TgChat. */
    @Transactional
    @Override
    public void unregister(Long tgChatId) {
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat == null) {
            throw new NotFoundException("Чат '" + tgChatId + "' не найден");
        }
        List<Link> trackedLinks = context.select(Tables.LINK.fields()).from(Tables.CHAT_LINK)
            .join(Tables.CHAT).on(Tables.CHAT.ID.eq(Tables.CHAT_LINK.CHAT_ID))
            .join(Tables.LINK).on(Tables.LINK.ID.eq(Tables.CHAT_LINK.LINK_ID))
            .where(Tables.CHAT.ID.eq(tgChat.getId())).fetchInto(Link.class);
        for (Link link : trackedLinks) {
            context.deleteFrom(Tables.CHAT_LINK).where(Tables.CHAT_LINK.LINK_ID.eq(link.getId()))
                .and(Tables.CHAT_LINK.CHAT_ID.eq(tgChat.getId())).execute();
            int count = context.selectCount().from(Tables.CHAT_LINK)
                .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId()).and(Tables.CHAT_LINK.CHAT_ID.notEqual(tgChat.getId())))
                .fetchOne(0, int.class);
            if (count == 0) {
                context.deleteFrom(Tables.LINK).where(Tables.LINK.ID.eq(link.getId())).execute();
            }
        }
        context.deleteFrom(Tables.CHAT).where(Tables.CHAT.TG_CHAT_ID.eq(tgChatId)).execute();
    }

    /**
     * метод, который возвращает объект TgChat с заданным идентификатором,
     * если он существует, или null в противном случае
     */
    private TgChat getTgChat(Long tgChatId) {
        List<TgChat> chats = context.select(Tables.CHAT.fields()).from(Tables.CHAT)
            .where(Tables.CHAT.TG_CHAT_ID.eq(tgChatId)).fetchInto(TgChat.class);
        if (chats.size() == 0) {
            return null;
        }
        return chats.get(0);
    }
}
