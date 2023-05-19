package ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Getter и Setter — это аннотации Lombok, которые генерируют методы получения и установки для полей класса
 */
@Getter
@Setter
/**
 * Entity — это аннотация JPA, указывающая, что класс является
 * постоянной сущностью и должен быть сопоставлен с таблицей базы данных
 */
@Entity
/**
 * Table используется для указания имени таблицы базы данных, с которой должен быть сопоставлен объект
 */
@Table(name = "chat")
public class TgChatEntity {
    /**
     * Id и GeneratedValue используются, чтобы указать,
     * что это первичный ключ таблицы и должен автоматически генерироваться базой данных
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * поле, представляющее идентификатор чата Telegram.
     * Аннотация Column, чтобы указать, что он сопоставлен со столбцом в таблице*/
    @Column(name = "tg_chat_id")
    private Long tgChatId;
}
