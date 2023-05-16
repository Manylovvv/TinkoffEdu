package ru.tinkoff.edu.java.scrapper.domain.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;


@Component
public class TgMapper implements RowMapper<TgChat> {
    @Override
    public TgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TgChat(rs.getLong("id"), rs.getLong("tg_chat_id"));
    }
}
