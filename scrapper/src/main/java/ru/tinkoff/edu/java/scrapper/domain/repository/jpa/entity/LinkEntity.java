package ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "link")
public class LinkEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "link")
    private String link;

    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;

    @Column(name = "last_activity")
    private OffsetDateTime lastActivity;

    @Column(name = "open_issues_count")
    private Integer openIssuesCount;

    @Column(name = "answer_count")
    private Integer answerCount;
}
