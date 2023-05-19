package ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Getter и Setter — это аннотации Lombok, которые генерируют
 * методы получения и установки для полей класса
 */
@Getter
@Setter
/**Entity — это аннотация JPA, указывающая, что класс является постоянной сущностью
 * и должен быть сопоставлен с таблицей базы данных
  */
@Entity
/**
 * Table используется для указания имени таблицы базы данных, с которой должен быть сопоставлен объект
 */
@Table(name = "link")
public class LinkEntity {
    /**
     * Id и GeneratedValue используются, чтобы указать,
     * что это первичный ключ таблицы и должен автоматически генерироваться базой данных
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * строковое поле, представляющее URL-адрес ссылки
     * Аннотация Column, чтобы указать, что он сопоставлен со столбцом в таблице
     */
    @Column(name = "link")
    private String link;

    /**
     * поле OffsetDateTime, представляющее дату и время последнего обновления ссылки
     * Аннотация Column, чтобы указать, что он сопоставлен со столбцом в таблице*/
    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;

    /**
     * поле OffsetDateTime, представляющее дату и время последней активности ссылки
     * Аннотация Column, чтобы указать, что он сопоставлен со столбцом в таблице
     */
    @Column(name = "last_activity")
    private OffsetDateTime lastActivity;

    /**
     * целочисленное поле, представляющее количество открытых проблем, связанных со ссылкой
     * Аннотация Column, чтобы указать, что он сопоставлен со столбцом в таблице
     */
    @Column(name = "open_issues_count")
    private Integer openIssuesCount;

    /**
     * целочисленное поле, представляющее количество ответов, предоставленных для ссылки
     * Аннотация Column, чтобы указать, что он сопоставлен со столбцом в таблице
     */
    @Column(name = "answer_count")
    private Integer answerCount;
}
