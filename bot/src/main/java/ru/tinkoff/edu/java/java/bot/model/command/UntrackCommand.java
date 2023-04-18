package ru.tinkoff.edu.java.java.bot.model.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;

@AllArgsConstructor
public class UntrackCommand implements Command {
    private final ScrapperClient client;

    @Override
    public String command() {
        return "untrack";
    }

    @Override
    public String description() {
        return "Прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage process(Update update) {
        if (update.message().text().substring(1).equals(command())) {
            return new SendMessage(update.message().chat().id(), "Введите ссылку для прекращения отслеживания");
        }
        boolean result = client.deleteTrackedLink(update.message().chat().id(), update.message().text());
        if (!result) {
            return new SendMessage(update.message().chat().id(), "Произошла ошибка\nПроверьте, что введенная ссылка верна, и попробуйте еще раз");
        }
        return new SendMessage(update.message().chat().id(), "Ссылка успешно удалена из списка для отслеживания");
    }
}
