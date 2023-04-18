package ru.tinkoff.edu.java.java.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.java.bot.dto.request.LinkUpdate;


@RestController()
public class BotController {

    @PostMapping("/updates")
    public void update(@RequestBody LinkUpdate request) {
        System.out.println(request.getUrl());
        System.out.println(request.getDescription());
        System.out.println(request.getTgChatIds());
    }
}
