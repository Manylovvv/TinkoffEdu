package ru.tinkoff.edu.java.scrapper.api.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.parser.data.GitHubLinkData;
import ru.tinkoff.edu.java.scrapper.api.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.api.response.github.GitHubApiResponse;

public class HttpGitHubClient implements GitHubClient {

    private static final String GIT_URL = "https://api.github.com/repos/";
    private static final String PATH_DELIMITER = "/";

    private final String GitUrl;
    private final WebClient webClient;

    public HttpGitHubClient(WebClient webClient) {
        this.webClient = webClient;
        GitUrl = GIT_URL;
    }

    public HttpGitHubClient(String baseUrl, WebClient webClient) {
        this.GitUrl = baseUrl;
        this.webClient = webClient;
    }

    @Override
    public GitHubApiResponse GetRepo(GitHubLinkData userAndRepo) {
        return webClient
                .get()
                .uri(GitUrl + userAndRepo.user_name() + PATH_DELIMITER + userAndRepo.repository_name())
                .retrieve().bodyToMono(GitHubApiResponse.class).block();
    }

}