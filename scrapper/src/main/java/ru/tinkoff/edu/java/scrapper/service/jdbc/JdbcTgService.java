package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.AllArgsConstructor;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
public class JdbcTgService implements TgChatService {
    private final ChatLinkRepository repository;

    /**метод принимает идентификатор чата tg и регистрирует чат с помощью объекта ChatLinkRepository*/
    @Override
    public void register(Long tgChatId) {
        repository.registerChat(tgChatId);
    }

    /**метод принимает идентификатор чата tg и отменяет регистрацию чата с помощью объекта ChatLinkRepository*/
    @Override
    public void unregister(Long tgChatId) {
        repository.unregisterChat(tgChatId);
    }
}
