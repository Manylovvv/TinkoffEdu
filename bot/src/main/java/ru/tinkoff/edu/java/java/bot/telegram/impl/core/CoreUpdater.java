package ru.tinkoff.edu.java.java.bot.telegram.impl.core;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.TrackCommand;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.UntrackCommand;
import ru.tinkoff.edu.java.java.bot.telegram.command.interfaces.Command;

/**
 * @Param List<? extends Command> commands список универсального команд
 * @Param List<Long> untrackRequest = new ArrayList<>(); список для отмены отслеживания
 * @Param List<Long> trackRequest = new ArrayList<>(); список для запроса отслеживания
 */
public class CoreUpdater {
    private final List<? extends Command> commands;
    private final List<Long> untrackRequest = new ArrayList<>();
    private final List<Long> trackRequest = new ArrayList<>();

    public CoreUpdater(Command... commands) {
        this.commands = Arrays.stream(commands).toList();
    }

    /**
     * Метод processCommand принимает объект Update и обрабатывает содержащуюся в нем команду.
     * Если команда начинается с «/», она удаляет идентификатор чата из списков trackRequest и
     * untrackRequest, извлекает строку команды и находит соответствующий объект Command в списке
     * команд. Если объект Command является TrackCommand или UntrackCommand, он добавляет
     * идентификатор чата в соответствующий список. Наконец, он вызывает метод процесса объекта
     * Command и возвращает результат в виде объекта SendMessage. Если команда не распознана,
     * она возвращает сообщение об ошибке по умолчанию.
     * @param update
     */
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
