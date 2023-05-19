package ru.tinkoff.edu.java.java.bot.telegram.impl.creator;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import java.util.Arrays;
import java.util.List;
import ru.tinkoff.edu.java.java.bot.telegram.command.interfaces.Command;
import ru.tinkoff.edu.java.java.bot.telegram.impl.core.CoreUpdater;

public class BotCreator implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final CoreUpdater processor;

    /**
     * конструктор бота
     * @param token - токен бота
     * @param commands - список команд
     */
    public BotCreator(String token, Command... commands) {
        telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(this);
        telegramBot.execute(new SetMyCommands(Arrays.stream(commands).map(Command::toBotCommand)
            .toArray(BotCommand[]::new)));
        processor = new CoreUpdater(commands);
    }

    /**
     * Метод sendMessages принимает строку описания и список идентификаторов чата и отправляет
     * описание каждому идентификатору чата с помощью метода SendMessage объекта TelegramBot
     * @param description - описание команд
     * @param tgChatIds - список идентификаторов чата
     */
    public void sendMessages(String description, List<Long> tgChatIds) {
        for (Long tgChatId : tgChatIds) {
            telegramBot.execute(new SendMessage(tgChatId, description));
        }
    }

    /**
     * Метод process принимает список объектов Update, обрабатывает каждый из них с помощью
     * метода processCommand объекта CoreUpdater и отправляет полученный объект SendMessage
     * с помощью объекта TelegramBot
     * @param list - список апдейтов
     */
    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            telegramBot.execute(processor.processCommand(update));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
