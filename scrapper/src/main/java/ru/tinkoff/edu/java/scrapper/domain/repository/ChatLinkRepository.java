package ru.tinkoff.edu.java.scrapper.domain.repository;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.domain.repository.mapper.Mapper;
import ru.tinkoff.edu.java.scrapper.domain.repository.mapper.TgMapper;
import ru.tinkoff.edu.java.scrapper.excontroller.exception.NotFoundException;

/**
 * Генерирует параметризованный конструктор, который
 * принимает один параметр для каждого поля и инициализирует их с его помощью
 */
@AllArgsConstructor
/**Аннотация, которая указывает, что это компонент репозитория данных Спринга*/
@Repository
public class ChatLinkRepository {
    private final LinkRepository linkRepository;
    private final TgChatRepository tgChatRepository;
    private final JdbcTemplate jdbcTemplate;
    private final TgMapper tgMapper;
    private final Mapper mapper;

    /**
     * Метод добавляет в базу данных новый чат
     */
    public void registerChat(Long tgChatId) {
        tgChatRepository.add(tgChatId);
    }

    /**
     * Метод unregisterChat() удаляет чат из базы данных и не отслеживает все ссылки, отслеживаемые чатом
     */
    @Transactional
    public void unregisterChat(Long tgChatId) {
        List<Link> trackedLinks = getAllLinks(tgChatId);
        for (Link link : trackedLinks) {
            untrackLink(tgChatId, link.getLink());
        }
        tgChatRepository.remove(tgChatId);
    }

    /**
     * Метод getAllLinks() извлекает из базы данных все ссылки, отслеживаемые чатом
     */
    @Transactional
    public List<Link> getAllLinks(Long tgChatId) {
        TgChat tgChat = tgChatRepository.get(tgChatId);
        if (tgChat == null) {
            throw new NotFoundException("Tg chat '" + tgChatId + "' was not found");
        }
        return jdbcTemplate.query("select * from link where id in (select link_id from chat_link cl "
            + "join chat c on cl.chat_id=c.id where c.id=?)", mapper, tgChat.getId());
    }

    /**
     * Метод trackLink() добавляет новую ссылку в базу данных и связывает ее с чатом
     */
    @Transactional
    public Link trackLink(Long tgChatId, Link url) {
        Link link = linkRepository.get(url.getLink());
        if (link == null) {
            link = linkRepository.add(url);
        }
        TgChat tgChat = tgChatRepository.get(tgChatId);
        if (tgChat == null) {
            throw new NotFoundException("Tg chat '" + tgChatId + "' was not found");
        }
        Integer rowCount =
            jdbcTemplate.queryForObject("select count(*) from chat_link where chat_id=? and link_id=?", Integer.class,
                tgChat.getId(), link.getId()
            );
        if (rowCount != null && !rowCount.equals(0)) {
            throw new RuntimeException("Link '" + url + "' is already tracking by tg chat '" + tgChatId + "'");
        }
        jdbcTemplate.update("insert into chat_link (chat_id, link_id) values (?, ?)", tgChat.getId(), link.getId());
        return link;
    }

    /**
     * Метод untrackLink() удаляет ссылку из базы данных и отменяет ее связь с чатом
     */
    @Transactional
    public Link untrackLink(Long tgChatId, URI url) {
        Link link = linkRepository.get(url);
        if (link == null) {
            throw new NotFoundException("Link '" + url + "' was not found");
        }
        TgChat tgChat = tgChatRepository.get(tgChatId);
        if (tgChat == null) {
            throw new NotFoundException("Tg chat '" + tgChatId + "' was not found");
        }
        int rowCount =
            jdbcTemplate.update("delete from chat_link where chat_id=? and link_id=?", tgChat.getId(), link.getId());
        if (rowCount == 0) {
            throw new RuntimeException("Link '" + url + "' is not tracking by tg chat '" + tgChatId + "'");
        }
        if (getLinkCount(link.getId()).equals(0)) {
            linkRepository.remove(link.getLink());
        }
        return link;
    }

    /**
     * Метод getChatsForLink() извлекает из базы данных все чаты, которые отслеживают ссылки
     */
    public List<TgChat> getChatsForLink(Link link) {
        return jdbcTemplate.query(
            "select * from chat where id in (select chat_id from chat_link where link_id=?)",
            tgMapper,
            link.getId()
        );
    }

    private Integer getLinkCount(Long linkId) {
        return jdbcTemplate.queryForObject("select count(*) from chat_link where link_id=?", Integer.class, linkId);
    }
}
