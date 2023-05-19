package ru.tinkoff.edu.java.scrapper.domain.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;

/**
 * Mapper, реализующий интерфейс RowMapper, который используется для сопоставления строк данных
 * из запроса к базе данных с объектами Java
 */
@Component
public class TgMapper implements RowMapper<TgChat> {
    /**
     * Метод mapRow принимает объект ResultSet и целочисленный параметр
     * и возвращает объект TgChat, созданный из данных в ResultSet
     */
    @Override
    public TgChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TgChat(rs.getLong("id"), rs.getLong("tg_chat_id"));
    }
}
