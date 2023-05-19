package ru.tinkoff.edu.java.scrapper.domain.repository;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.domain.repository.mapper.TgMapper;

/**Аннотация генерирует параметризованный конструктор*/
@AllArgsConstructor
/**Показывает, что класс применяется для работы с поиском, а также для получения и хранения данных*/
@Repository
public class TgChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TgMapper tgMapper;

    /**
     * Извлекает все объекты TgChat из базы данных и возвращает их в виде списка.
     */
    public List<TgChat> findAll() {
        return jdbcTemplate.query("select * from chat", tgMapper);
    }

    /**
     * Удаляет объект TgChat из базы данных на основе его tg_chat_id
     */
    public void remove(Long tgChatId) {
        jdbcTemplate.update("delete from chat where tg_chat_id=?", tgChatId);
    }

    /**
     * Добавляет новый объект TgChat в базу данных с предоставленным tg_chat_id
     */
    public void add(Long tgChatId) {
        jdbcTemplate.update("insert into chat (tg_chat_id) values (?)", tgChatId);
    }

    /**
     * Извлекает объект TgChat из базы данных на основе его tg_chat_id и возвращает его
     * Если нет объекта с предоставленным tg_chat_id, он возвращает null
     */
    public TgChat get(Long tgChatId) {
        try {
            return jdbcTemplate.queryForObject("select * from chat where tg_chat_id=?", tgMapper, tgChatId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
