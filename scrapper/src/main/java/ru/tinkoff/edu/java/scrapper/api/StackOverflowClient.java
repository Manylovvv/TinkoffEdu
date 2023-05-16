package ru.tinkoff.edu.java.scrapper.api;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionsResponse;

import java.time.Duration;

public class StackOverflowClient {
    private final String BASE_URL = "https://api.stackexchange.com/2.3/questions/";

    private final WebClient WEB_CLIENT;

    public StackOverflowClient() {
        WEB_CLIENT = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public StackOverflowClient(String url) {
        WEB_CLIENT = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public QuestionResponse getQuestionInfo(Long questionId) {
        return WEB_CLIENT.get()
                .uri(uriBuilder -> uriBuilder.path(questionId.toString())
                        .queryParam("site", "stackoverflow")
                        .build())
                .retrieve()
                .bodyToMono(QuestionsResponse.class)
                .timeout(Duration.ofSeconds(10))
                .block()
                .items()
                .get(0);
    }
}
