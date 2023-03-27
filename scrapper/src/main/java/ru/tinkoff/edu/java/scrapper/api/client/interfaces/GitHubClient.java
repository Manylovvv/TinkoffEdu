package ru.tinkoff.edu.java.scrapper.api.client.interfaces;

import ru.tinkoff.edu.java.parser.data.GitHubLinkData;
import ru.tinkoff.edu.java.scrapper.api.response.github.GitHubApiResponse;

public interface GitHubClient {
    GitHubApiResponse GetRepo(GitHubLinkData userAndRepo);
}
