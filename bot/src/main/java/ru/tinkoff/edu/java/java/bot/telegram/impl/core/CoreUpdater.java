package ru.tinkoff.edu.java.java.bot.telegram.impl.core;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.TrackCommand;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.UntrackCommand;
import ru.tinkoff.edu.java.java.bot.telegram.command.interfaces.Command;

public class CoreUpdater {
    private final List<? extends Command> commands;
    private final List<Long> untrackRequest = new ArrayList<>();
    private final List<Long> trackRequest = new ArrayList<>();

    public CoreUpdater(Command... commands) {
        this.commands = Arrays.stream(commands).toList();
    }

    public SendMessage processCommand(Update update) {
        if (update.message().text().startsWith("/")) {
            trackRequest.remove(update.message().chat().id());
            untrackRequest.remove(update.message().chat().id());
            String command = update.message().text().substring(1);
            Command processor = commands.stream().filter(el -> el.command().equals(command))
                .findAny().orElse(null);
            if (processor == null) {
                return invalidCommandMessage(update);
            } else {
                if (processor instanceof TrackCommand) {
                    trackRequest.add(update.message().chat().id());
                }
                if (processor instanceof UntrackCommand) {
                    untrackRequest.add(update.message().chat().id());
                }
                return processor.process(update);
            }
        }
        if (trackRequest.contains(update.message().chat().id())) {
            return commands.stream().filter(el -> el.command().equals("track")).findFirst().get().process(update);
        }
        if (untrackRequest.contains(update.message().chat().id())) {
            return commands.stream().filter(el -> el.command().equals("untrack")).findFirst().get().process(update);
        }
        return invalidCommandMessage(update);
    }

    private SendMessage invalidCommandMessage(Update update) {
        return new SendMessage(update.message().chat().id(), "Неизвестная команда, введите /help для просмотра"
            + "доступных команд!");
    }
}