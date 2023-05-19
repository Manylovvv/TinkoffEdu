package ru.tinkoff.edu.java.scrapper.domain.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.ChatLinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.TgChatEntity;

public interface ChatLinkEntityRepository extends JpaRepository<ChatLinkEntity, Long> {
    /**
     * Метод удаляет ChatLinkEntity по его свойствам TgChatEntity и LinkEntity
     * */
    void deleteByTgChatAndLink(TgChatEntity tgChat, LinkEntity link);

    /**
     * Query позволяет выполнять пользовательские запросы SQL
     * */
    @Query("select cle.tgChat from ChatLinkEntity cle where cle.link.id=:id")
    /**
     * getTgChatsByLinkId возвращает список объектов TgChatEntity, связанных с данным идентификатором LinkEntity
     */
    List<TgChatEntity> getTgChatsByLinkId(@Param("id") Long linkId);

    /**
     * Query, что позволяет выполнять пользовательские запросы SQL
     * */
    @Query("select cle.link from ChatLinkEntity cle where cle.tgChat.tgChatId=:id")
    /**
     * getLinksByTgChatId возвращает список объектов LinkEntity,
     * связанных с данным идентификатором TgChatEntity
     */
    List<LinkEntity> getLinksByTgChatId(@Param("id") Long tgChatId);

    /**
     * findByTgChatAndLink возвращает необязательный объект ChatLinkEntity,
     * который соответствует заданным TgChatEntity и LinkEntity
     */
    Optional<ChatLinkEntity> findByTgChatAndLink(TgChatEntity tgChat, LinkEntity link);
}
