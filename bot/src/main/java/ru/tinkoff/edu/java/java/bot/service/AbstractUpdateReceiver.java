package ru.tinkoff.edu.java.java.bot.service;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;
import ru.tinkoff.edu.java.java.bot.controller.dto.request.LinkUpdate;

@AllArgsConstructor
public abstract class AbstractUpdateReceiver {
    private BotCreator bot;

    public abstract void receiveUpdate(LinkUpdate request);

    protected void sendUpdates(LinkUpdate request) {
        bot.sendMessages(request.getDescription(), request.getTgChatIds());
    }
}
