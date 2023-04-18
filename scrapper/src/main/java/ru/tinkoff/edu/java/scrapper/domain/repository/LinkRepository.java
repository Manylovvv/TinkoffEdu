package ru.tinkoff.edu.java.scrapper.domain.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.mapper.LinkMapper;
import ru.tinkoff.edu.java.scrapper.exception.ResourceNotFoundException;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@AllArgsConstructor
@Repository
public class LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkMapper linkMapper;

    public List<Link> findAllForUpdate() {
        return findAll().stream().filter(link -> link.getLastUpdate().isBefore(OffsetDateTime.of(LocalDateTime.now().minusMinutes(5)
                , ZoneOffset.UTC))).toList();
    }

    public List<Link> findAll() {
        return jdbcTemplate.query("select * from link", linkMapper);
    }

    public Link add(Link url) {
        jdbcTemplate.update("insert into link (link, last_update, last_activity, open_issues_count, answer_count) " +
                        "values (?, ?, ?, ?, ?)", url.getLink().toString(), url.getLastUpdate(), url.getLastActivity(),
                url.getOpenIssuesCount(), url.getAnswerCount());
        return get(url.getLink());
    }

    public void remove(URI url) {
        Link link = get(url);
        if (link == null) {
            throw new ResourceNotFoundException("Link '" + url + "' was not found");
        }
        jdbcTemplate.update("delete from link where link=?", url.toString());
    }

    public Link get(URI url) {
        try {
            return jdbcTemplate.queryForObject("select * from link where link=?", linkMapper, url.toString());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void update(Link link) {
        int rowCount = jdbcTemplate.update("update link set last_update=?, last_activity=?, open_issues_count=?, answer_count=? where id=?",
                link.getLastUpdate(), link.getLastActivity(), link.getOpenIssuesCount(), link.getAnswerCount(), link.getId());
        if (rowCount == 0) {
            throw new RuntimeException("Error while updating link '" + link.getLink() + "'");
        }
    }
}
