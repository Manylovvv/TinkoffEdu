package ru.tinkoff.edu.java.scrapper.service.renew;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.parsers.GitHubLinkParser;
import ru.tinkoff.edu.java.parser.parsers.StackOverflowLinkParser;
import ru.tinkoff.edu.java.parser.records.GitHubRecord;
import ru.tinkoff.edu.java.parser.records.StackOverflowRecord;
import ru.tinkoff.edu.java.scrapper.api.GitHubClient;
import ru.tinkoff.edu.java.scrapper.api.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.RepositoryResponse;

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
/**показывает, что класс представляет собой сервис для реализации бизнес-логики*/
@Service
public class LinkRenew {
    private StackOverflowClient stackOverflowClient;
    private GitHubClient gitHubClient;
    private final GitHubLinkParser gitHubLinkParser = new GitHubLinkParser(null);
    private final StackOverflowLinkParser stackOverflowLinkParser = new StackOverflowLinkParser(gitHubLinkParser);

    /**
     * принимает объект URI, представляющий ссылку, и возвращает новый объект LinkEntity.
     * Он использует метод parseLink класса StackOverflowLinkParser для анализа ссылки и определения того,
     * является ли она ссылкой Stack Overflow или GitHub. Если это ссылка GitHub,
     * она использует метод getRepoInfo класса GitHubClient для получения информации
     * о репозитории и установки свойств lastActivity и openIssuesCount объекта LinkEntity.
     * Если это ссылка Stack Overflow, она использует метод getQuestionInfo
     * класса StackOverflowClient для получения информации о вопросе и установки свойств lastActivity
     * и answerCount объекта LinkEntity. Наконец, он возвращает новый объект LinkEntity.*/
    public LinkEntity createLinkEntity(URI url) {
        Record apiRecord = stackOverflowLinkParser.parseLink(url.toString());
        if (apiRecord == null) {
            throw new RuntimeException("Неккоректная ссылка '" + url + "'");
        }
        LinkEntity link = new LinkEntity();
        link.setLink(url.toString());
        link.setLastUpdate(OffsetDateTime.now());
        if (apiRecord instanceof GitHubRecord) {
            RepositoryResponse response = gitHubClient.getRepoInfo(
                ((GitHubRecord) apiRecord).username(),
                ((GitHubRecord) apiRecord).repo()
            );
            link.setLastActivity(response.updated_at());
            link.setOpenIssuesCount(response.open_issues_count());
        }
        if (apiRecord instanceof StackOverflowRecord) {
            QuestionResponse response =
                stackOverflowClient.getQuestionInfo(((StackOverflowRecord) apiRecord).questionId());
            link.setLastActivity(response.last_activity_date());
            link.setAnswerCount(response.answer_count());
        }
        return link;
    }

    /**Аналогичен созданию сущности, но возвращает новый объект Link вместо объекта LinkEntity.*/
    public Link createLink(URI url) {
        Record apiRecord = stackOverflowLinkParser.parseLink(url.toString());
        if (apiRecord == null) {
            throw new RuntimeException("Неккоректная ссылка '" + url + "'");
        }
        Link link = new Link();
        link.setLink(url);
        link.setLastUpdate(OffsetDateTime.now());
        if (apiRecord instanceof GitHubRecord) {
            RepositoryResponse response = gitHubClient.getRepoInfo(
                ((GitHubRecord) apiRecord).username(),
                ((GitHubRecord) apiRecord).repo()
            );
            link.setLastActivity(response.updated_at());
            link.setOpenIssuesCount(response.open_issues_count());
        }
        if (apiRecord instanceof StackOverflowRecord) {
            QuestionResponse response =
                stackOverflowClient.getQuestionInfo(((StackOverflowRecord) apiRecord).questionId());
            link.setLastActivity(response.last_activity_date());
            link.setAnswerCount(response.answer_count());
        }
        return link;
    }

    /**
     * принимает объект GitHubRecord и возвращает объект RepositoryResponse.
     * Он использует метод getRepoInfo класса GitHubClient
     * для получения информации о репозитории и возвращает ответ.
     */
    public RepositoryResponse getResponse(GitHubRecord gitHubRecord) {
        return gitHubClient.getRepoInfo(gitHubRecord.username(), gitHubRecord.repo());
    }

    /**
     * принимает объект Link и возвращает объект Record.
     * Он использует метод parseLink класса StackOverflowLinkParser для анализа ссылки и определения того,
     * является ли она ссылкой Stack Overflow или GitHub. Наконец, он возвращает объект Record.
     */
    public Record getRecord(Link link) {
        return stackOverflowLinkParser.parseLink(link.getLink().toString());
    }

    /**
     * Принимает объект StackOverflowRecord и возвращает объект QuestionResponse.
     * Он использует метод getQuestionInfo класса StackOverflowClient для получения
     * информации о вопросе и возвращает ответ
     */
    public QuestionResponse getResponse(StackOverflowRecord stackOverflowRecord) {
        return stackOverflowClient.getQuestionInfo(stackOverflowRecord.questionId());
    }
}

