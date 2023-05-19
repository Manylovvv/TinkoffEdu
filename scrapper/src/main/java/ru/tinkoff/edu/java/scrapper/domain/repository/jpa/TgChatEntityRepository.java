package ru.tinkoff.edu.java.scrapper.domain.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.TgChatEntity;

public interface TgChatEntityRepository extends JpaRepository<TgChatEntity, Long> {
    /**
     * findByTgChatId, возвращает необязательный объект TgChatEntity,
     * который соответствует заданному значению tgChatId
     */
    Optional<TgChatEntity> findByTgChatId(Long tgChatId);

    /**
     * Аннотация @Modifying указывает, что этот метод изменяет базу данных,
     * а аннотация @Query обеспечивает выполнение пользовательского SQL-запроса
     */
    @Modifying
    @Query(value = "insert into chat (tg_chat_id) values (:tg_chat_id) on conflict do nothing",
           nativeQuery = true)
    //Второй метод, insertTgChat, вставляет в базу данных новый объект TgChatEntity с заданным значением tg_chat_id
    void insertTgChat(@Param("tg_chat_id") Long tgChatId);
}
