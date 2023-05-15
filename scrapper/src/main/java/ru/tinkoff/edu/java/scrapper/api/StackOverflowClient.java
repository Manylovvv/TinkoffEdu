package ru.tinkoff.edu.java.scrapper.api;

import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionsResponse;

public class StackOverflowClient {
    private final String baseUrl = "https://api.stackexchange.com/2.3/questions/";
    private final WebClient webClient;
    private final int timeout = 10;

    public StackOverflowClient() {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public StackOverflowClient(String url) {
        webClient = WebClient.builder()
            .baseUrl(url)
            .build();
    }

    public QuestionResponse getQuestionInfo(Long questionId) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(questionId.toString())
                .queryParam("site", "stackoverflow")
                .build())
            .retrieve()
            .bodyToMono(QuestionsResponse.class)
            .timeout(Duration.ofSeconds(timeout))
            .block()
            .items()
            .get(0);
    }
}
