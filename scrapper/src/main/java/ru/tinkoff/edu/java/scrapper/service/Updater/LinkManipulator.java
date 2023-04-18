package ru.tinkoff.edu.java.scrapper.service.Updater;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.RepositoryResponse;
import ru.tinkoff.edu.java.parser.parsers.GitHubLinkParser;
import ru.tinkoff.edu.java.parser.parsers.StackOverflowLinkParser;
import ru.tinkoff.edu.java.parser.record.GitHubRecord;
import ru.tinkoff.edu.java.parser.record.StackOverflowRecord;
import ru.tinkoff.edu.java.scrapper.api.GitHubClient;
import ru.tinkoff.edu.java.scrapper.api.StackOverflowClient;

import java.net.URI;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Service
public class LinkManipulator {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final GitHubLinkParser gitHubLinkParser = new GitHubLinkParser(null);
    private final StackOverflowLinkParser stackOverflowLinkParser = new StackOverflowLinkParser(gitHubLinkParser);

    public Link createLink(URI url) {
        Record record = stackOverflowLinkParser.parseLink(url.toString());
        if (record == null) {
            throw new RuntimeException("Invalid link '" + url + "'");
        }
        Link link = new Link();
        link.setLink(url);
        link.setLastUpdate(OffsetDateTime.now());
        if (record instanceof GitHubRecord) {
            RepositoryResponse response = gitHubClient.getRepoInfo(((GitHubRecord) record).username(),
                    ((GitHubRecord) record).repo());
            link.setLastActivity(response.updated_at());
            link.setOpenIssuesCount(response.open_issues_count());
        }
        if (record instanceof StackOverflowRecord) {
            QuestionResponse response = stackOverflowClient.getQuestionInfo(((StackOverflowRecord) record).questionId());
            link.setLastActivity(response.last_activity_date());
            link.setAnswerCount(response.answer_count());
        }
        return link;
    }

    public Record getRecord(Link link) {
        return stackOverflowLinkParser.parseLink(link.getLink().toString());
    }

    public RepositoryResponse getResponse(GitHubRecord record) {
        return gitHubClient.getRepoInfo(record.username(), record.repo());
    }

    public QuestionResponse getResponse(StackOverflowRecord record) {
        return stackOverflowClient.getQuestionInfo(record.questionId());
    }
}
