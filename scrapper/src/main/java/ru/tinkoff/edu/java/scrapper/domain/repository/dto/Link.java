package ru.tinkoff.edu.java.scrapper.domain.repository.dto;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
/**Генерирует параметризованный конструктор, который принимает
 *  один параметр для каждого поля и инициализирует их с его помощью
 */
@AllArgsConstructor
/**
 * используется для создания конструктора без аргументов
 */
@NoArgsConstructor
public class Link {
    private Long id;
    private URI link;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime lastActivity;
    private Integer openIssuesCount;
    private Integer answerCount;
}
