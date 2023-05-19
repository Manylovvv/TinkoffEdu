package ru.tinkoff.edu.java.scrapper.domain.repository.response;

import java.time.OffsetDateTime;

/**
 * Записи для хранения данных, возвращаемых Stack Exchange
 */
public record QuestionResponse(Long question_id, OffsetDateTime last_activity_date, Integer answer_count) { }
