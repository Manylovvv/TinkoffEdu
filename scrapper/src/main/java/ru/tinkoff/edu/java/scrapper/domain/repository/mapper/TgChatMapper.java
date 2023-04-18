package ru.tinkoff.edu.java.scrapper.domain.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TgChatMapper implements RowMapper<TgChat> {
    @Override
    public TgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TgChat(rs.getLong("id"), rs.getLong("tg_chat_id"));
    }
}
