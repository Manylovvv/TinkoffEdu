package ru.tinkoff.edu.java.scrapper.domain.repository.response;

import java.time.OffsetDateTime;

public record RepositoryResponse(String full_name,
                                 OffsetDateTime updated_at,
                                 Integer open_issues_count) { }
