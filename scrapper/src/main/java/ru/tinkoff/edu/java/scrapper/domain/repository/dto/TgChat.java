package ru.tinkoff.edu.java.scrapper.domain.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Генерирует параметризованный конструктор, который принимает
 * один параметр для каждого поля и инициализирует их с его помощью
 */
@AllArgsConstructor
public class TgChat {
    private Long id;
    private Long tgChatId;
}
