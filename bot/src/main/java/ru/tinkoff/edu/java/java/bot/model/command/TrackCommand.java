package ru.tinkoff.edu.java.java.bot.model.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;

@AllArgsConstructor
public class TrackCommand implements Command {
    private final ScrapperClient client;

    @Override
    public String command() {
        return "track";
    }

    @Override
    public String description() {
        return "Начать отслеживание ссылки";
    }

    @Override
    public SendMessage process(Update update) {
        if (update.message().text().substring(1).equals(command())) {
            return new SendMessage(update.message().chat().id(), "Введите ссылку для отслеживания");
        }
        boolean result = client.addTrackedLink(update.message().chat().id(), update.message().text());
        if (!result) {
            return new SendMessage(update.message().chat().id(), "Ошибка!\nПроверьте, что введенная ссылка верна, и попробуйте еще раз");
        }
        return new SendMessage(update.message().chat().id(), "Ссылка успешно добавлена в список для отслеживания");
    }
}
