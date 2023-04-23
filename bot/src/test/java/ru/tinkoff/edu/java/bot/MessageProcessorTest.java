package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.model.command.ListCommand;
import ru.tinkoff.edu.java.bot.model.core.CoreUpdater;
import ru.tinkoff.edu.java.bot.scrapper.api.ScrapperClient;
import ru.tinkoff.edu.java.bot.scrapper.api.model.AllLinksApiResponse;
import ru.tinkoff.edu.java.bot.scrapper.api.model.LinkResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageProcessorTest {
    @Mock
    private ScrapperClient client;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    private CoreUpdater messageProcessor;

    @BeforeEach
    public void setUp() {
        messageProcessor = new CoreUpdater(new ListCommand(client));
    }

    @Test
    public void processInvalidCommand() {
        Long chatId = 999666L;
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/invalid");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        SendMessage message = messageProcessor.processCommand(update);
        Assertions.assertEquals(chatId, message.getParameters().get("chat_id"));
        Assertions.assertEquals("<b>Неизвестная команда!</b>\n" +
                " Чтобы получить список доступных команд, используйте команду /help", message.getParameters().get("text"));
    }

    @Test
    public void processListCommand_fullList() throws URISyntaxException {
        Long chatId = 888888L;
        LinkResponse link1 = new LinkResponse(1L, new URI("https://github.com/sanyarnd/tinkoff-java-course-2022/"));
        LinkResponse link2 = new LinkResponse(2L, new URI("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"));
        AllLinksApiResponse response = new AllLinksApiResponse(List.of(link1, link2), 2);
        String messageText = "Вы отслеживаете следующие ссылки:\n"
                 + link1.url().toString() + "\n"
                 + link2.url().toString() + "\n";

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(client.getAllLinks(anyLong())).thenReturn(response);
        SendMessage message = messageProcessor.processCommand(update);
        Assertions.assertEquals(messageText, message.getParameters().get("text"));
        Assertions.assertEquals(chatId, message.getParameters().get("chat_id"));
    }

    @Test
    public void processListCommand_emptyList() {
        Long chatId = 102102L;
        AllLinksApiResponse response = new AllLinksApiResponse(Collections.emptyList(), 0);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(client.getAllLinks(anyLong())).thenReturn(response);
        SendMessage message = messageProcessor.processCommand(update);
        String emptyListMessage = "Список отслеживаемых ссылок пуст!";
        Assertions.assertEquals(emptyListMessage, message.getParameters().get("text"));
        Assertions.assertEquals(chatId, message.getParameters().get("chat_id"));
    }
}
