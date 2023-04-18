package ru.tinkoff.edu.java.scrapper.api;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.RepositoryResponse;

import java.time.Duration;

public class GitHubClient {
    private final String BASE_URL = "https://api.github.com/repos/";

    private final WebClient WEB_CLIENT;

    public GitHubClient() {
        WEB_CLIENT = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public GitHubClient(String url) {
        WEB_CLIENT = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public RepositoryResponse getRepoInfo(String username, String repo) {
        return WEB_CLIENT.get()
                .uri("{username}/{repo}", username, repo)
                .retrieve()
                .bodyToMono(RepositoryResponse.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    }
}
