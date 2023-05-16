package ru.tinkoff.edu.java.scrapper.domain.repository.mapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;

@Component
public class Mapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new Link(rs.getLong("id"), new URI(rs.getString("link")),
                rs.getObject("last_update", OffsetDateTime.class), rs.getObject("last_activity", OffsetDateTime.class),
                rs.getInt("open_issues_count"), rs.getInt("answer_count")
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
