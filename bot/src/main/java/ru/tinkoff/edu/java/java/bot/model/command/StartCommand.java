package ru.tinkoff.edu.java.java.bot.model.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;

@AllArgsConstructor
public class StartCommand implements Command {
    private final ScrapperClient client;

    @Override
    public String command() {
        return "start";
    }

    @Override
    public String description() {
        return "Зарегистрировать пользователя";
    }

    @Override
    public SendMessage process(Update update) {
        client.registerChat(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), getMessageText(update.message().chat().firstName()))
                .parseMode(ParseMode.HTML);
    }

    private String getMessageText(String name) {
        return "Приветствую вас, <i>" + name + "</i>!\n"
                + "Чтобы получить список доступных команд, используйте команду /help";
    }
}
