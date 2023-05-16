package ru.tinkoff.edu.java.java.bot.service;

import ru.tinkoff.edu.java.java.bot.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.java.bot.model.core.BotCreator;

public class HttpLinkUpdateReceiver extends LinkUpdateReceiver {
    public HttpLinkUpdateReceiver(BotCreator bot) {
        super(bot);
    }

    @Override
    public void receiveUpdate(LinkUpdate request) {
        this.sendUpdates(request);
    }
}
