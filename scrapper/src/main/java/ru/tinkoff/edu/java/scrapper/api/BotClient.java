package ru.tinkoff.edu.java.scrapper.api;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdate;


import java.time.Duration;

public class BotClient {
    private final String BASE_URL = "http://localhost:8080/";

    private final WebClient WEB_CLIENT;

    public BotClient() {
        WEB_CLIENT = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public BotClient(String url) {
        WEB_CLIENT = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public void sendUpdate(LinkUpdate request) {
        WEB_CLIENT.post()
                .uri("updates")
                .body(Mono.just(request), LinkUpdate.class)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    }
}
