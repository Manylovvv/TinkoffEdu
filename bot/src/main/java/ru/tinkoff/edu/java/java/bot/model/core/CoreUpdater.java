package ru.tinkoff.edu.java.java.bot.model.core;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.java.bot.model.command.TrackCommand;
import ru.tinkoff.edu.java.java.bot.model.command.Command;
import ru.tinkoff.edu.java.java.bot.model.command.UntrackCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoreUpdater {
    private final List<? extends Command> commands;
    private final List<Long> trackRequest = new ArrayList<>();
    private final List<Long> untrackRequest = new ArrayList<>();

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
            }
            else {
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
        return new SendMessage(update.message().chat().id(), "Неизвестная команда");
    }
}
