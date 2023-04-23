package ru.tinkoff.edu.java.bot.scrapper.api;

import ru.tinkoff.edu.java.bot.scrapper.api.model.AllLinksApiResponse;
import ru.tinkoff.edu.java.bot.scrapper.api.model.LinkResponse;

public interface ScrapperClient {

    void registerChat(long chatId);

    void deleteChat(long chatId);

    AllLinksApiResponse getAllLinks(long tgChatId);

    LinkResponse addLink(long tgChatId, String link);

    LinkResponse deleteLink(long tgChatId, String link);
}
