package ru.tinkoff.edu.java.java.bot.telegram.command.interfaces;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {

    String command();

    String description();

    SendMessage process(Update update);

    default BotCommand toBotCommand() {
        return new BotCommand(command(), description());
    }
}
