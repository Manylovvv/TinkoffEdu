package ru.tinkoff.edu.java.bot.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.bot.schemas.model.LinkUpdate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/updates")
public class BotController {

 private final List<LinkUpdate> LinkUpdates = new ArrayList<>();

    @PostMapping
    public String updateChat(@RequestBody LinkUpdate update){
        LinkUpdates.add(update);
    return "Chat was updated!";
    }

}
