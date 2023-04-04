package ru.tinkoff.edu.java.bot.model.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.bot.scrapper.api.ScrapperClient;
import ru.tinkoff.edu.java.bot.scrapper.api.exception.ApiClientErrorException;
import ru.tinkoff.edu.java.bot.scrapper.api.exception.ApiInternalServerErrorException;
import ru.tinkoff.edu.java.bot.scrapper.api.model.AllLinksApiResponse;

@AllArgsConstructor
public class ListCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage process(Update update) {
        AllLinksApiResponse listLinksResponse;
        try {
           listLinksResponse = scrapperClient.getAllLinks(update.message().chat().id());
        } catch (ApiClientErrorException e) {
            return new SendMessage(update.message().chat().id(),"Error!");
        } catch (ApiInternalServerErrorException e) {
            return new SendMessage(update.message().chat().id(),"Error!");
        }
        var builder = new StringBuilder();
        if (listLinksResponse.size() == 0) {
            builder.append("Список отслеживаемых ссылок пуст!");
        } else {
            builder.append("Вы отслеживаете следующие ссылки:\n");
            listLinksResponse.links().forEach(x -> builder.append(x.url()).append("\n"));
        }
        return new SendMessage(update.message().chat().id(), builder.toString());
    }

}
