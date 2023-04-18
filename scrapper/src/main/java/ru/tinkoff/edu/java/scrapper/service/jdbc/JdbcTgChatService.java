package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.domain.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.TgChatService;
@Primary
@AllArgsConstructor
@Service
public class JdbcTgChatService implements TgChatService {
    private final ChatLinkRepository repository;

    @Override
    public void register(Long tgChatId) {
        repository.registerChat(tgChatId);
    }

    @Override
    public void unregister(Long tgChatId) {
        repository.unregisterChat(tgChatId);
    }
}
