package ru.tinkoff.edu.java.java.bot.service;

import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;
import ru.tinkoff.edu.java.java.bot.controller.dto.request.LinkUpdate;

public class HttpUpdateReceiver extends AbstractUpdateReceiver {
    public HttpUpdateReceiver(BotCreator bot) {
        super(bot);
    }

    @Override
    public void receiveUpdate(LinkUpdate request) {
        this.sendUpdates(request);
    }
}
