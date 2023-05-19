package ru.tinkoff.edu.java.scrapper.domain.repository.response;

import java.time.OffsetDateTime;

/**
 * Записи для хранения данных, возвращаемых Гитом
 */
public record RepositoryResponse(String full_name, OffsetDateTime updated_at, Integer open_issues_count) { }
