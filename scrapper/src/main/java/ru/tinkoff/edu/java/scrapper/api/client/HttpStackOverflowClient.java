package ru.tinkoff.edu.java.scrapper.api.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.parser.data.StackOverflowLinkData;
import ru.tinkoff.edu.java.scrapper.api.client.interfaces.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.api.response.stackoverflow.StackOverflowItemApiResponse;
import ru.tinkoff.edu.java.scrapper.api.response.stackoverflow.StackOverflowRootApiResponse;

public class HttpStackOverflowClient implements StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com/2.3/questions/";
    private static final String STACKOVERFLOW_MANDATORY_REQUEST_PARAMS = "?order=desc&sort=activity&site=stackoverflow";

    private final String baseUrl;
    private final WebClient webClient;

    public HttpStackOverflowClient(WebClient webClient) {
        this.webClient = webClient;
        baseUrl = BASE_URL;
    }

    public HttpStackOverflowClient(String baseUrl, WebClient webClient) {
        this.baseUrl = baseUrl;
        this.webClient = webClient;
    }

    @Override
    public StackOverflowItemApiResponse GetQuestion(StackOverflowLinkData id) {
        var response = webClient.get()
                .uri(baseUrl + id.question_id() + STACKOVERFLOW_MANDATORY_REQUEST_PARAMS)
                .retrieve().bodyToMono(StackOverflowRootApiResponse.class).block();
        return response.items().get(0);
    }
}
