package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.ChatLinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.LinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.TgChatEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.ChatLinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.TgChatEntity;


import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ChatLinkEntityRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private TgChatEntityRepository tgChatEntityRepository;
    @Autowired
    private LinkEntityRepository linkEntityRepository;
    @Autowired
    private ChatLinkEntityRepository chatLinkEntityRepository;
    private final List<TgChatEntity> tgChatEntities = new ArrayList<>();
    private final List<LinkEntity> linkEntities = new ArrayList<>();

    @BeforeEach
    public void setup() {
        tgChatEntities.clear();
        linkEntities.clear();
        long id = 1234567L;
        for (int i = 0; i < 3; i++) {
            TgChatEntity tgChatEntity = new TgChatEntity();
            tgChatEntity.setTgChatId(id++);
            tgChatEntities.add(tgChatEntityRepository.save(tgChatEntity));
        }
        for (int i = 0; i < 3; i++) {
            LinkEntity linkEntity = new LinkEntity();
            linkEntity.setLink("link" + i);
            linkEntity.setLastActivity(OffsetDateTime.now());
            linkEntity.setLastUpdate(OffsetDateTime.now());
            linkEntities.add(linkEntityRepository.save(linkEntity));
        }
        for (int i = 0; i < tgChatEntities.size(); i++) {
            for (int j = i; j < linkEntities.size(); j++) {
                ChatLinkEntity entity = new ChatLinkEntity();
                entity.setTgChat(tgChatEntities.get(i));
                entity.setLink(linkEntities.get(j));
                chatLinkEntityRepository.save(entity);
            }
        }
    }

    @Test
    @Transactional
    @Rollback
    public void getTgChatsByLinkId() {
        for (int i = 0; i < linkEntities.size(); i++) {
            List<TgChatEntity> result = chatLinkEntityRepository.getTgChatsByLinkId(linkEntities.get(i).getId());
            Assertions.assertEquals(i + 1, result.size());
            for (int j = 0; j < result.size(); j++) {
                Assertions.assertEquals(tgChatEntities.get(j).getTgChatId(), result.get(j).getTgChatId());
            }
        }
    }

    @Test
    @Transactional
    @Rollback
    public void getLinksByTgChatId() {
        for (int i = 0; i < tgChatEntities.size(); i++) {
            List<LinkEntity> result = chatLinkEntityRepository.getLinksByTgChatId(tgChatEntities.get(i).getTgChatId());
            Assertions.assertEquals(tgChatEntities.size() - i, result.size());
            for (int j = 0; j < result.size(); j++) {
                Assertions.assertEquals(linkEntities.get(j + i).getLink(), result.get(j).getLink());
            }
        }
    }
}
