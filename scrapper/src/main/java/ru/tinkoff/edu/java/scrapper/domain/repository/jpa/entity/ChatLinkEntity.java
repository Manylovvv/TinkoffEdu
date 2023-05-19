package ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Getter и Setter — это аннотации Lombok,
 * которые генерируют методы получения и установки для полей класса
 */
@Getter
@Setter
/**
 * Entity — это аннотация JPA, указывающая, что класс является постоянной
 * сущностью и должен быть сопоставлен с таблицей базы данных
 */
@Entity
/**
 * Table используется для указания имени таблицы базы данных, с которой должен быть сопоставлен объект
 */
@Table(name = "chat_link")
public class ChatLinkEntity {
    /**
     * Id и GeneratedValue используются, чтобы указать,
     * что это первичный ключ таблицы и должен автоматически генерироваться базой данных
     * Аннотация Column, чтобы указать, что он сопоставлен со столбцом в таблице
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * OneToOne и JoinColumn используются, чтобы указать,
     * что он сопоставлен с одной строкой в таблице tg_chat
     */
    @OneToOne
    @JoinColumn(name = "chat_id")
    private TgChatEntity tgChat;

    /**
     * OneToOne и JoinColumn используются, чтобы указать,
     * что он сопоставлен с одной строкой в таблице link
     */
    @OneToOne
    @JoinColumn(name = "link_id")
    private LinkEntity link;
}
