package ru.tinkoff.edu.java.java.bot.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.java.bot.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.java.bot.service.AbstractLinkUpdateReceiver;

@AllArgsConstructor
@RestController
public class BotController {
    private AbstractLinkUpdateReceiver linkUpdateReceiver;

    @PostMapping("/updates")
    public void update(@RequestBody LinkUpdate request) {
        linkUpdateReceiver.receiveUpdate(request);
    }
}
