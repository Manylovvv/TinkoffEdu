package ru.tinkoff.edu.java.scrapper.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;

@AllArgsConstructor
@RestController()
@RequestMapping("/tg-chat")
public class TgChatController {
    private final JdbcTgChatService service;

    @PostMapping(value = "/{id}")
    public void registerChat(@PathVariable("id") Long id) {
        service.register(id);
    }

    @DeleteMapping(value = "{id}")
    public void deleteChat(@PathVariable("id") Long id) {
        service.unregister(id);
    }
}
