package ru.tinkoff.edu.java.java.bot.model.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import ru.tinkoff.edu.java.java.bot.model.command.Command;

import java.util.Arrays;
import java.util.List;

public class BotCreator implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final CoreUpdater processor;

    public BotCreator(String token, Command... commands) {
        telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(this);
        telegramBot.execute(new SetMyCommands(Arrays.stream(commands).map(Command::toBotCommand).toArray(BotCommand[]::new)));
        processor = new CoreUpdater(commands);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update: list) {
            telegramBot.execute(processor.processCommand(update));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
