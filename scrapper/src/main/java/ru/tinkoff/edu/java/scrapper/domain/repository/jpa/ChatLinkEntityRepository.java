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
    void deleteByTgChatAndLink(TgChatEntity tgChat, LinkEntity link);

    Optional<ChatLinkEntity> findByTgChatAndLink(TgChatEntity tgChat, LinkEntity link);

    @Query("select cle.tgChat from ChatLinkEntity cle where cle.link.id=:id")
    List<TgChatEntity> getTgChatsByLinkId(@Param("id") Long linkId);

    @Query("select cle.link from ChatLinkEntity cle where cle.tgChat.tgChatId=:id")
    List<LinkEntity> getLinksByTgChatId(@Param("id") Long tgChatId);
}
