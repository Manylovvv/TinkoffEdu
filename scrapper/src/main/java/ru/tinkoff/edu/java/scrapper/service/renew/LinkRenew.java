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

@AllArgsConstructor
@Service
public class LinkRenew {
    private StackOverflowClient stackOverflowClient;
    private GitHubClient gitHubClient;
    private final GitHubLinkParser gitHubLinkParser = new GitHubLinkParser(null);
    private final StackOverflowLinkParser stackOverflowLinkParser = new StackOverflowLinkParser(gitHubLinkParser);

    public LinkEntity createLinkEntity(URI url) {
        Record apiRecord = stackOverflowLinkParser.parseLink(url.toString());
        if (apiRecord == null) {
            throw new RuntimeException("Invalid link '" + url + "'");
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

    public Link createLink(URI url) {
        Record apiRecord = stackOverflowLinkParser.parseLink(url.toString());
        if (apiRecord == null) {
            throw new RuntimeException("Invalid link '" + url + "'");
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

    public RepositoryResponse getResponse(GitHubRecord gitHubRecord) {
        return gitHubClient.getRepoInfo(gitHubRecord.username(), gitHubRecord.repo());
    }

    public Record getRecord(Link link) {
        return stackOverflowLinkParser.parseLink(link.getLink().toString());
    }

    public QuestionResponse getResponse(StackOverflowRecord stackOverflowRecord) {
        return stackOverflowClient.getQuestionInfo(stackOverflowRecord.questionId());
    }
}

