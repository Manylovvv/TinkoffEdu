package ru.tinkoff.edu.java.bot.model.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.bot.scrapper.api.ScrapperClient;
import ru.tinkoff.edu.java.bot.scrapper.api.exception.ApiClientErrorException;
import ru.tinkoff.edu.java.bot.scrapper.api.exception.ApiInternalServerErrorException;


@AllArgsConstructor
public class UntrackCommand implements Command {
    private final ScrapperClient scrapperClient;

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
        try {
            scrapperClient.deleteLink(update.message().chat().id(), update.message().text());
        } catch (ApiClientErrorException e) {
            return new SendMessage(update.message().chat().id(),"Такой ссылки не было добавлено!");
        } catch (ApiInternalServerErrorException e) {
            return new SendMessage(update.message().chat().id(),"Error!");
        }
        return new SendMessage(update.message().chat().id(), "Ссылка успешно удалена из списка для отслеживания");
    }
}
