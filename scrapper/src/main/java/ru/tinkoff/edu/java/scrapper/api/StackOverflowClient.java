package ru.tinkoff.edu.java.scrapper.api;

import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionsResponse;

public class StackOverflowClient {
    private final int timeout = 10;
    private final String baseUrl = "https://api.stackexchange.com/2.3/questions/";
    private final WebClient webClient;

    /**
     * Установка базового URL и создание объекта Вебклиент
     */
    public StackOverflowClient() {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * Инициализация объекта и установка базового URL-адреса
     */
    public StackOverflowClient(String url) {
        webClient = WebClient.builder().baseUrl(url).build();
    }

    /**
     * Отправка реквеста на сервак через вебклиент.
     * post -> uri -> bodytomono (тело запроса) -> timeout (время ожидания сервака) -> block (ожидание ответа)
     */
    public QuestionResponse getQuestionInfo(Long questionId) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path(questionId.toString())
                .queryParam("site", "stackoverflow").build()).retrieve().bodyToMono(QuestionsResponse.class)
            .timeout(Duration.ofSeconds(timeout)).block().items().get(0);
    }
}
