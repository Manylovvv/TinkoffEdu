package ru.tinkoff.edu.java.java.bot.telegram.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.java.bot.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;
import ru.tinkoff.edu.java.java.bot.telegram.command.interfaces.Command;

@AllArgsConstructor
public class ListCommand implements Command {
    private final ScrapperClient client;

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
        ListLinksResponse response = client.getListLinks(update.message().chat().id());
        if (response.getLinks() == null) {
            return new SendMessage(update.message().chat().id(), "Ошибка, попробуйте еще раз");
        }
        if (response.getSize() == 0) {
            return new SendMessage(update.message().chat().id(), "Список отслеживаемых ссылок пуст!");
        }
        return new SendMessage(update.message().chat().id(), setMessage(response));
    }

    private String setMessage(ListLinksResponse list) {
        StringBuilder result = new StringBuilder("Список отслеживаемых ссылок:\n");
        for (int i = 0; i < list.getSize(); i++) {
            result.append(i + 1).append(". ").append(list.getLinks().get(i).getUrl().toString()).append("\n");
        }
        return result.toString();
    }
}
