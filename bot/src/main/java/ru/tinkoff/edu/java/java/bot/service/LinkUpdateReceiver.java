package ru.tinkoff.edu.java.java.bot.service;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.java.bot.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.java.bot.model.core.BotCreator;


@AllArgsConstructor
public abstract class LinkUpdateReceiver {
    private BotCreator bot;

    public abstract void receiveUpdate(LinkUpdate request);

    protected void sendUpdates(LinkUpdate request) {
        bot.sendMessages(request.getDescription(), request.getTgChatIds());
    }
}
