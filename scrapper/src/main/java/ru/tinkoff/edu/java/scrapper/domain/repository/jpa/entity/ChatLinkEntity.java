package ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat_link")
public class ChatLinkEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "chat_id")
    private TgChatEntity tgChat;

    @OneToOne
    @JoinColumn(name = "link_id")
    private LinkEntity link;
}
