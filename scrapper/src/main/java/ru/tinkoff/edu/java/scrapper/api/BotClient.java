package ru.tinkoff.edu.java.scrapper.api;

import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.controller.dto.request.LinkUpdate;

public class BotClient {
    private final int timeout = 10;
    private final String baseUrl = "http://localhost:8080/";
    private final WebClient webClient;

    /**Установка базового URL и создание объекта Вебклиент*/
    public BotClient() {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    /**Инициализация объекта и установка базового URL-адреса*/
    public BotClient(String url) {
        webClient = WebClient.builder().baseUrl(url).build();
    }

    /**
     * Отправка реквеста на сервак через вебклиент.
     * post -> uri -> bodytomono (тело запроса) -> timeout (время ожидания сервака) -> block (ожидание ответа)
     */
    public void sendUpdate(LinkUpdate request) {
        webClient.post().uri("updates").body(Mono.just(request), LinkUpdate.class).retrieve()
            .bodyToMono(Void.class).timeout(Duration.ofSeconds(timeout)).block();
    }
}
