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
