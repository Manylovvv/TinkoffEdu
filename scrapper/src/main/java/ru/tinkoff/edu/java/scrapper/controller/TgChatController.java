package ru.tinkoff.edu.java.scrapper.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;

/**
 * Аннотация, указывающая, что конструктор с аргументами для всех полей должен быть сгенерирован Lombok
 */
@AllArgsConstructor
/**Аннотация Springa, указывающая, что класс является
 * контроллером и что его методы должны возвращать данные,
 * которые можно сериализовать в тело ответа
 */
@RestController
/**Аннотация, которая указывает базовый URL-адрес для всех запросов, обрабатываемых контроллером*/
@RequestMapping("/tg-chat")
public class TgChatController {
    private final TgChatService service;

    /**
     * Аннотация означает, что он обрабатывает запросы DELETE с переменной пути id
     */
    @DeleteMapping(value = "{id}")
    public void deleteChat(@PathVariable("id") Long id) {
        service.unregister(id);
    }

    /**
     * Аннотация означает, что он обрабатывает запросы POST с переменной пути id
     */
    @PostMapping(value = "/{id}")
    public void registerChat(@PathVariable("id") Long id) {
        service.register(id);
    }
}
