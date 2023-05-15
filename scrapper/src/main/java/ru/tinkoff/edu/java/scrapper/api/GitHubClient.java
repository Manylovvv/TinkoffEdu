package ru.tinkoff.edu.java.scrapper.api;

import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.RepositoryResponse;

public class GitHubClient {
    private final String baseUrl = "https://api.github.com/repos/";
    private final WebClient webClient;
    private final int timeout = 10;

    public GitHubClient() {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public GitHubClient(String url) {
        webClient = WebClient.builder()
            .baseUrl(url)
            .build();
    }

    public RepositoryResponse getRepoInfo(String username, String repo) {
        return webClient.get()
            .uri("{username}/{repo}", username, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .timeout(Duration.ofSeconds(timeout))
            .block();
    }
}

