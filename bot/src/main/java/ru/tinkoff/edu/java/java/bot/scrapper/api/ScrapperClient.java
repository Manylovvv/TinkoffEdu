package ru.tinkoff.edu.java.java.bot.scrapper.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.java.bot.controller.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.java.bot.controller.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.java.bot.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.java.bot.controller.dto.response.ListLinksResponse;

/**
 * Класс ScrapperClient имеет два конструктора: один использует базовый URL-адрес
 * по умолчанию (http://localhost:8081/), а другой — пользовательский базовый URL-адрес.
 * Доступ к веб-службе осуществляется с помощью Spring WebClient API.
 */
public class ScrapperClient {
    private final String baseUrl = "http://localhost:8081/";
    private final WebClient webClient;
    private final int timeout = 10;

    public ScrapperClient() {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public ScrapperClient(String url) {
        webClient = WebClient.builder().baseUrl(url).build();
    }

    /**
     * отправляет POST-запрос веб-службе для регистрации
     * чата Telegram с заданным идентификатором.
     * @param id - индентификатор чата
     */
    public void registerChat(Long id) {
        webClient.post()
            .uri("/tg-chat/" + id.toString()).retrieve()
            .bodyToMono(Void.class).timeout(Duration.ofSeconds(timeout)).block();
    }

    /**
     * отправляет GET-запрос веб-службе для получения списка отслеживаемых
     * ссылок для указанного идентификатора чата Telegram
     * @param id - идентификатор чата
     */
    public ListLinksResponse getListLinks(Long id) {
        return webClient.get()
            .uri("links").header("Tg-Chat-Id", id.toString()).retrieve()
            .bodyToMono(ListLinksResponse.class).timeout(Duration.ofSeconds(timeout))
            .onErrorReturn(new ListLinksResponse()).block();
    }

    /**
     * отправляет POST-запрос веб-службе, чтобы добавить новую отслеживаемую
     * ссылку для указанного идентификатора чата Telegram.
     * @param id - идентификатор чата
     * @param link - ссылка
     * @return - булинова переменная обозначающее свойство добавления ссылки
     */
    public boolean addTrackedLink(Long id, String link) {
        AddLinkRequest request;
        try {
            request = new AddLinkRequest(new URI(link));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LinkResponse response = webClient.post()
            .uri("links").header("Tg-Chat-Id", id.toString())
            .body(Mono.just(request), AddLinkRequest.class).retrieve()
            .bodyToMono(LinkResponse.class).timeout(Duration.ofSeconds(timeout))
            .onErrorReturn(new LinkResponse()).block();
        return response != null && response.getUrl() != null && response.getUrl().toString().equals(link);
    }

    /**
     * отправляет запрос DELETE в веб-службу на удаление отслеживаемой ссылки для указанного идентификатора
     * чата Telegram.
     * @param id - идентификатор чата
     * @param link - ссылка
     * @return - Возвращает логическое значение, указывающее, успешно ли удалена ссылка.
     */
    public boolean deleteTrackedLink(Long id, String link) {
        RemoveLinkRequest request;
        try {
            request = new RemoveLinkRequest(new URI(link));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LinkResponse response = webClient.method(HttpMethod.DELETE)
            .uri("links").header("Tg-Chat-Id", id.toString())
            .body(Mono.just(request), RemoveLinkRequest.class).retrieve()
            .bodyToMono(LinkResponse.class).timeout(Duration.ofSeconds(timeout))
            .onErrorReturn(new LinkResponse()).block();
        return response != null && response.getUrl() != null && response.getUrl().toString().equals(link);
    }
}
