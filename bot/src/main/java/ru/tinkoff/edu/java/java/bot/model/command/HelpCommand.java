package ru.tinkoff.edu.java.java.bot.model.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {
    @Override
    public String command() {
        return "help";
    }

    @Override
    public String description() {
        return "Вывести окно с командами";
    }

    @Override
    public SendMessage process(Update update) {
        return new SendMessage(update.message().chat().id(), getMessageText()).parseMode(ParseMode.HTML);
    }

    private String getMessageText() {
        return "<b>Список доступных команд:</b>\n"
                + "/start -- регистрирует пользователя\n"
                + "/help -- выводит окно с доступными командами\n"
                + "/track -- добавляет ссылку в список отслеживаемых\n"
                + "/untrack -- удаляет ссылку из списка отслеживаемых\n"
                + "/list -- позволяет получить список всех отслеживаемых на данный момент ссылок";
    }
}
