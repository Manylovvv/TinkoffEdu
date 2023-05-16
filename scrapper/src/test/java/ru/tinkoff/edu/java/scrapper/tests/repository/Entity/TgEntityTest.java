package ru.tinkoff.edu.java.scrapper.tests.repository.Entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.abstracts.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.TgChatEntityRepository;

import java.util.List;

@SpringBootTest
public class TgEntityTest extends IntegrationEnvironment {

    @Autowired
    private TgChatEntityRepository tgChatEntityRepository;
    private final List<Long> tgChatIds = List.of(123456L, 1234567L);

    @Test
    @Transactional
    @Rollback
    public void insertTgChat() {
        for (Long tgChatId : tgChatIds) {
            for (int j = 0; j < 3; j++) {
                tgChatEntityRepository.insertTgChat(tgChatId);
            }
        }
        System.out.println(tgChatEntityRepository.findAll());
        Assertions.assertEquals(tgChatIds.size(), tgChatEntityRepository.findAll().size());
        Assertions.assertEquals(tgChatIds.get(0), tgChatEntityRepository.findAll().get(0).getTgChatId());
        Assertions.assertEquals(tgChatIds.get(1), tgChatEntityRepository.findAll().get(1).getTgChatId());
    }
}
