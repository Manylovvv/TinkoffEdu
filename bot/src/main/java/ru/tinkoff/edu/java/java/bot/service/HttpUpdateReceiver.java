package ru.tinkoff.edu.java.java.bot.service;

import ru.tinkoff.edu.java.java.bot.controller.dto.request.LinkUpdate;
import ru.tinkoff.edu.java.java.bot.telegram.impl.creator.BotCreator;

/**
 * предназначен для получения обновлений через HTTP-запросы.
 * Он вызывает метод receiveUpdate() для обработки обновления и отправки
 * обновлений боту Telegram с помощью метода sendUpdates(),
 * унаследованного от класса AbstractUpdateReceiver.
 */
public class HttpUpdateReceiver extends AbstractUpdateReceiver {
    public HttpUpdateReceiver(BotCreator bot) {
        super(bot);
    }

    @Override
    public void receiveUpdate(LinkUpdate request) {
        this.sendUpdates(request);
    }
}
