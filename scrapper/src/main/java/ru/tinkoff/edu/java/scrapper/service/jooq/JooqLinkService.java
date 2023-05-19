package ru.tinkoff.edu.java.scrapper.service.jooq;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.domain.jooq.Tables;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.excontroller.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.refactor.Refactor;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
public class JooqLinkService implements LinkService {
    /**экземпляр класса DSLContext, который используется для взаимодействия с базой данных с помощью библиотеки jOOQ*/
    private DSLContext context;
    /**методы для преобразования одного типа данных в другой*/
    private final Refactor refactor;
    /**методы для создания или извлечения ссылочных объектов и ссылок*/
    private final LinkRenew linkRenew;
    private final ApplicationConfig config;

    /**
     * этот метод принимает объект Link в качестве входных данных и возвращает список объектов TgChat,
     * связанных со ссылкой. Он делает это, выбирая соответствующие строки из таблиц CHAT_LINK и CHAT с помощью jOOQ,
     * а затем загружая результаты в список объектов TgChat
     */
    @Override
    public List<TgChat> getChatsForLink(Link link) {
        return context.select(Tables.CHAT.fields()).from(Tables.CHAT_LINK).join(Tables.CHAT)
            .on(Tables.CHAT.ID.eq(Tables.CHAT_LINK.CHAT_ID))
            .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId())).fetchInto(TgChat.class);
    }

    /**Аннотация, чтобы гарантировать, что они выполняются внутри транзакции.
     * этот метод принимает в качестве входных данных объект Long, представляющий идентификатор чата Telegram,
     * и объект URI, представляющий URL-адрес ссылки, и удаляет связь между чатом и ссылкой из базы данных.
     * Он делает это, удаляя соответствующие строки из таблицы CHAT_LINK с помощью jOOQ, а затем проверяя,
     * связаны ли какие-либо другие чаты со ссылкой. Если нет, он также удаляет объект ссылки из таблицы LINK.
     * Наконец, он возвращает объект LinkResponse, содержащий информацию об удаленной ссылке.
     */
    @Transactional
    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        Link link = getLink(url);
        if (link == null) {
            throw new NotFoundException("Ссылка '" + url + "' не найдена");
        }
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat == null) {
            throw new NotFoundException("Чат '" + tgChatId + "' не найден");
        }
        context.deleteFrom(Tables.CHAT_LINK)
            .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId())).and(Tables.CHAT_LINK.CHAT_ID.eq(tgChat.getId()))
            .execute();
        int count = context.selectCount().from(Tables.CHAT_LINK)
            .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId()).and(Tables.CHAT_LINK.CHAT_ID.notEqual(tgChat.getId())))
            .fetchOne(0, int.class);
        if (count == 0) {
            context.deleteFrom(Tables.LINK).where(Tables.LINK.ID.eq(link.getId())).execute();
        }
        return refactor.linkToLinkResponse(link);
    }

    /**Аннотация, чтобы гарантировать, что они выполняются внутри транзакции
     * этот метод принимает в качестве входных данных объект Long, представляющий идентификатор чата Telegram,
     * и возвращает список объектов Link, связанных с чатом. Это делается путем выбора соответствующих строк из
     * таблиц LINK и CHAT_LINK с помощью jOOQ, а затем выборки результатов в список объектов Link. Затем он
     * преобразует список объектов Link в объект ListLinksResponse, используя класс Refactor.*/
    @Transactional
    @Override
    public ListLinksResponse listAll(Long tgChatId) {
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat == null) {
            throw new NotFoundException("Чат '" + tgChatId + "' не найден");
        }
        return refactor.linksToListLinksResponse(context.select(Tables.LINK.fields()).from(Tables.LINK)
            .join(Tables.CHAT_LINK).on(Tables.CHAT_LINK.LINK_ID.eq(Tables.LINK.ID))
            .where(Tables.CHAT_LINK.CHAT_ID.eq(tgChat.getId())).fetchInto(Link.class));
    }

    /**
     * это частный вспомогательный метод, который принимает в качестве входных данных объект URI,
     * представляющий URL-адрес ссылки, и возвращает соответствующий объект Link из базы данных.
     * Это делается путем выбора соответствующих строк из таблицы LINK с помощью jOOQ, а затем выборки
     * результатов в список объектов Link. Если ссылки не найдены, возвращается null.
     */
    private Link getLink(URI url) {
        List<Link> links = context.select(Tables.LINK.fields()).from(Tables.LINK)
            .where(Tables.LINK.LINK_.eq(url.toString())).fetchInto(Link.class);
        if (links.size() == 0) {
            return null;
        }
        return links.get(0);
    }

    /**
     * это частный вспомогательный метод, который принимает в качестве входных данных объект Long,
     * представляющий идентификатор чата Telegram, и возвращает соответствующий объект TgChat из базы данных.
     * Он делает это, выбирая соответствующие строки из таблицы CHAT с помощью jOOQ, а затем загружая результаты
     * в список объектов TgChat. Если чаты не найдены, возвращается null.
     */
    private TgChat getTgChat(Long tgChatId) {
        List<TgChat> chats = context.select(Tables.CHAT.fields()).from(Tables.CHAT)
            .where(Tables.CHAT.TG_CHAT_ID.eq(tgChatId)).fetchInto(TgChat.class);
        if (chats.size() == 0) {
            return null;
        }
        return chats.get(0);
    }

    /**
     * этот метод принимает в качестве входных данных объект Long, представляющий идентификатор чата Telegram,
     * и объект URI, представляющий URL-адрес ссылки, и добавляет связь между чатом и ссылкой в базу данных.
     * Сначала он проверяет, существуют ли чат и ссылка в базе данных, и если нет,
     * генерирует исключение NotFoundException.
     * Если ссылка не существует, он создает новый объект ссылки с помощью класса
     * LinkRenew и вставляет его в таблицу LINK
     * с помощью jOOQ. Затем он извлекает только что вставленный объект ссылки из
     * базы данных. Если ссылка по-прежнему не существует,
     * выдается исключение RuntimeException. Если чат уже связан со ссылкой, выдается исключение
     * RuntimeException. В противном случае он вставляет новую строку в таблицу CHAT_LINK,
     * чтобы связать чат со ссылкой, и возвращает объект LinkResponse,
     * содержащий информацию о добавленной ссылке.
     * */
    @Transactional
    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        TgChat tgChat = getTgChat(tgChatId);
        if (tgChat == null) {
            throw new NotFoundException("Чат '" + tgChatId + "' не найден");
        }
        Link link = getLink(url);
        if (link == null) {
            Link newLink = linkRenew.createLink(url);
            context.insertInto(
                Tables.LINK,
                Tables.LINK.LINK_,
                Tables.LINK.LAST_UPDATE,
                Tables.LINK.LAST_ACTIVITY,
                Tables.LINK.ANSWER_COUNT,
                Tables.LINK.OPEN_ISSUES_COUNT
            ).values(newLink.getLink().toString(), newLink.getLastUpdate().toLocalDateTime(),
                newLink.getLastActivity().toLocalDateTime(), newLink.getAnswerCount(), newLink.getOpenIssuesCount()
            ).execute();
            link = getLink(url);
            if (link == null) {
                throw new RuntimeException("Ошибка в добавлении ссылки '" + url + "'");
            }
        } else {
            int count = context.selectCount().from(Tables.CHAT_LINK)
                .where(Tables.CHAT_LINK.LINK_ID.eq(link.getId()).and(Tables.CHAT_LINK.CHAT_ID.eq(tgChat.getId())))
                .fetchOne(0, int.class);
            if (count != 0) {
                throw new RuntimeException("Ссылка '" + url + "' уже отслеживается чатом'" + tgChatId + "'");
            }
        }
        context.insertInto(Tables.CHAT_LINK, Tables.CHAT_LINK.CHAT_ID, Tables.CHAT_LINK.LINK_ID)
            .values(tgChat.getId(), link.getId()).execute();
        return refactor.linkToLinkResponse(link);
    }

    /**
     * этот метод возвращает список объектов Link, которые необходимо обновить.
     * Он делает это, выбирая соответствующие строки из таблицы LINK с помощью jOOQ,
     * основываясь на том, меньше ли их поле LAST_UPDATE, чем текущее время минус интервал обновления,
     * указанный в объекте ApplicationConfig. Затем он извлекает результаты в список объектов Link.
     */
    @Override
    public List<Link> findLinksForUpdate() {
        return context.select(Tables.LINK.fields()).from(Tables.LINK)
            .where(Tables.LINK.LAST_UPDATE.lessThan(LocalDateTime.now().minusMinutes(config.getUpdateInterval())))
            .fetchInto(Link.class);
    }

    /**
     * этот метод принимает объект Link в качестве входных данных и обновляет его поля в базе данных.
     * Это делается путем обновления соответствующей строки в таблице LINK
     * с помощью jOOQ на основе поля идентификатора ссылки.
     */
    @Override
    public void updateLink(Link link) {
        context.update(Tables.LINK).set(Tables.LINK.LAST_UPDATE, link.getLastUpdate().toLocalDateTime())
            .set(Tables.LINK.LAST_ACTIVITY, link.getLastActivity().toLocalDateTime())
            .set(Tables.LINK.ANSWER_COUNT, link.getAnswerCount())
            .set(Tables.LINK.OPEN_ISSUES_COUNT, link.getOpenIssuesCount())
            .where(Tables.LINK.ID.eq(link.getId())).execute();
    }
}
