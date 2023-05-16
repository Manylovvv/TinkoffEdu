package ru.tinkoff.edu.java.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.java.bot.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.java.bot.scrapper.api.ScrapperClient;
import ru.tinkoff.edu.java.java.bot.telegram.command.impl.ListCommand;
import ru.tinkoff.edu.java.java.bot.telegram.impl.core.CoreUpdater;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CoreUpdaterTest {
    @Mock
    private ScrapperClient client;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat tgchat;
    private CoreUpdater messageProcessor;

    @BeforeEach
    public void setUp() {
        messageProcessor = new CoreUpdater(new ListCommand(client));
    }

    @Test
    public void processListCommand_emptyList() {
        Long chatId = 43215784L;
        String emptyListMessage = "Список отслеживаемых ссылок пуст!";
        ListLinksResponse response = new ListLinksResponse(Collections.emptyList(), 0);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/list");
        when(message.chat()).thenReturn(tgchat);
        when(tgchat.id()).thenReturn(chatId);
        when(client.getListLinks(anyLong())).thenReturn(response);
        SendMessage message1 = messageProcessor.processCommand(update);
        Assertions.assertEquals(chatId, message1.getParameters().get("chat_id"));
        Assertions.assertEquals(emptyListMessage, message1.getParameters().get("text"));
    }

    @Test
    public void processInvalidCommand() {
        Long chatId = 321321L;
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("/invalid");
        when(message.chat()).thenReturn(tgchat);
        when(tgchat.id()).thenReturn(chatId);
        SendMessage message = messageProcessor.processCommand(update);
        Assertions.assertEquals(chatId, message.getParameters().get("chat_id"));
        Assertions.assertEquals("Неизвестная команда, введите /help " +
            "для просмотрадоступных команд!", message.getParameters().get("text"));
    }
}
