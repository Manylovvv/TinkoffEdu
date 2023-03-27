package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.exception.IncorrectParametersException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/tg-chat")
public class TgController {

    private final Set<Long> TelegramIds = new HashSet<>();

    @PostMapping("/{id}")
    public String registerChat(@PathVariable long id) {
        if (!TelegramIds.add(id))
            throw new IncorrectParametersException(String.format("Chat was already exists", id));
        else return "Chat was registered";
    }

    @DeleteMapping("/{id}")
    public String deleteChat(@PathVariable long id) {
        if (!TelegramIds.remove(id))
            throw new IncorrectParametersException(String.format("Chat was doesn't exist", id));
        else return "Chat was deleted";
    }
}
