package ru.tinkoff.edu.java.scrapper.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.controller.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.controller.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;

/**Аннотация, указывающая, что конструктор с аргументами для всех полей должен быть сгенерирован Lombok*/
@AllArgsConstructor
/**Аннотация Springa, указывающая, что класс является контроллером
 *  и что его методы должны возвращать данные,которые можно сериализовать в тело ответа
 */
@RestController
/**Аннотация, которая указывает базовый URL-адрес для всех запросов,
 *  обрабатываемых контроллером
 */
@RequestMapping("/links")
public class LinksController {
    private final LinkService service;

    /**
     * Аннотация означает, что он обрабатывает запросы POST и выдает ответ в формате JSON
     * Метод вызывает метод add() объекта LinkService и возвращает результат в виде объекта LinkResponse
     * id, который представляет собой значение Long, полученное из заголовка запроса,
     * и request, который является экземпляром AddLinkRequest, полученным из тела запроса
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public LinkResponse addTrackedLink(@RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody AddLinkRequest request) {
        return service.add(id, request.getUrl());
    }

    /**
     * Аннотация означает, что он обрабатывает запросы GET и выдает ответ в формате JSON
     * id, который представляет собой значение Long, полученное из заголовка запроса
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ListLinksResponse getTrackedLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        return service.listAll(id);
    }

    /**
     * Аннотация означает, что он обрабатывает запросы DELETE и выдает ответ в формате JSON
     * id, который представляет собой значение Long, полученное из заголовка запроса,
     * и request, который является экземпляром RemoveLinkRequest, полученным из тела запроса
     */
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public LinkResponse deleteTrackedLink(@RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody RemoveLinkRequest request) {
        return service.remove(id, request.getUrl());
    }
}
