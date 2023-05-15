package ru.tinkoff.edu.java.scrapper.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;

@AllArgsConstructor
@RestController
@RequestMapping("/tg-chat")
public class TgChatController {
    private final TgChatService service;

    @PostMapping(value = "/{id}")
    public void registerChat(@PathVariable("id") Long id) {
        service.register(id);
    }

    @DeleteMapping(value = "{id}")
    public void deleteChat(@PathVariable("id") Long id) {
        service.unregister(id);
    }
}
