package ru.tinkoff.edu.java.scrapper.domain.repository;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.domain.repository.mapper.TgMapper;

@AllArgsConstructor
@Repository
public class TgChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TgMapper tgMapper;

    public List<TgChat> findAll() {
        return jdbcTemplate.query("select * from chat", tgMapper);
    }

    public void remove(Long tgChatId) {
        jdbcTemplate.update("delete from chat where tg_chat_id=?", tgChatId);
    }

    public void add(Long tgChatId) {
        jdbcTemplate.update("insert into chat (tg_chat_id) values (?)", tgChatId);
    }

    public TgChat get(Long tgChatId) {
        try {
            return jdbcTemplate.queryForObject("select * from chat where tg_chat_id=?", tgMapper, tgChatId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
