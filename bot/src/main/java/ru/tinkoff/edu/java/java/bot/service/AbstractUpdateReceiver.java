package ru.tinkoff.edu.java.java.bot.service;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.java.bot.controller.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;

@AllArgsConstructor
public abstract class AbstractUpdateReceiver {
    private BotCreator bot;

    /**
     * метод receiveUpdate() должен быть реализован его подклассами
     * для обработки обновления, и метод
     * sendUpdates(), который отправляет обработанное обновление боту
     */
    public abstract void receiveUpdate(LinkUpdate request);

    protected void sendUpdates(LinkUpdate request) {
        bot.sendMessages(request.getDescription(), request.getTgChatIds());
    }
}
