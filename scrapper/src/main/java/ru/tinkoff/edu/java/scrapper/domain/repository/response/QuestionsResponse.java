package ru.tinkoff.edu.java.scrapper.domain.repository.response;

import java.util.List;

/**
 * Записи для хранения данных, возвращаемых Stack Exchange
 */
public record QuestionsResponse(List<QuestionResponse> items) { }
