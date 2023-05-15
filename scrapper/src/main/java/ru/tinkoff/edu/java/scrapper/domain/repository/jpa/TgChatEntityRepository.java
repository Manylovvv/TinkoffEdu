package ru.tinkoff.edu.java.scrapper.domain.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.TgChatEntity;

public interface TgChatEntityRepository extends JpaRepository<TgChatEntity, Long> {
    Optional<TgChatEntity> findByTgChatId(Long tgChatId);

    @Modifying
    @Query(value = "insert into chat (tg_chat_id) values (:tg_chat_id) on conflict do nothing",
           nativeQuery = true)
    void insertTgChat(@Param("tg_chat_id") Long tgChatId);
}
