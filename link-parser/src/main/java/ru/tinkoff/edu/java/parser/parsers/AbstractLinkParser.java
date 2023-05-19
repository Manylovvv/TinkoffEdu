package ru.tinkoff.edu.java.parser.parsers;

import lombok.AllArgsConstructor;

/**
 * Аннотация, указывающая, что конструктор с аргументами для всех полей должен быть сгенерирован Lombok
 */
@AllArgsConstructor
public abstract sealed class AbstractLinkParser permits GitHubLinkParser, StackOverflowLinkParser {
    /**
     *  поле nextParser используется для объединения парсеров в цепочку.
     */
    protected AbstractLinkParser nextParser;

    /**
     * принимает параметр `String` и возвращает объект `Record`.
     */
    abstract Record parseLink(String link);
}
