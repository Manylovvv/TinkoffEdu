package ru.tinkoff.edu.java.scrapper.service.jpa;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.TgChat;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.ChatLinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.LinkEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.TgChatEntityRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.ChatLinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.TgChatEntity;
import ru.tinkoff.edu.java.scrapper.excontroller.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.refactor.Refactor;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
public class JpaLinkService implements LinkService {
    private final ApplicationConfig config;
    /**
     * объект интерфейса LinkEntityRepository, который предоставляет методы
     * для взаимодействия с объектами ссылок в базе данных.
     */
    private final LinkEntityRepository linkEntityRepository;
    /**
     * объект класса LinkRenew, предоставляющий методы для
     * создания и извлечения ссылочных объектов и ссылок.
     */
    private final LinkRenew linkRenew;
    private final Refactor refactor;
    private final TgChatEntityRepository tgChatEntityRepository;
    private final ChatLinkEntityRepository chatLinkEntityRepository;

    /**
     * чтобы гарантировать их выполнение в рамках транзакции
     * этот метод принимает объект Link в качестве входных данных и возвращает список объектов
     * TgChat, связанных со ссылкой. Он делает это, запрашивая в chatLinkEntityRepository объекты
     * ChatLinkEntity, которые имеют тот же идентификатор, что и входная ссылка,
     * а затем использует объект рефакторинга для преобразования полученных объектов TgChatEntity в объекты TgChat
     */
    @Transactional
    @Override
    public List<TgChat> getChatsForLink(Link link) {
        return chatLinkEntityRepository.getTgChatsByLinkId(link.getId()).stream().map(refactor::tgChatEntityToTgChat)
            .toList();
    }

    /**
     * этот метод принимает в качестве входных данных объект Long, представляющий
     * идентификатор чата Telegram, и объект URI, представляющий URL-адрес ссылки, и удаляет
     * связь между чатом и ссылкой из базы данных. Он делает это, запрашивая tgChatEntityRepository
     * для объекта TgChatEntity с тем же идентификатором,
     * что и входной идентификатор чата, и запрашивая linkEntityRepository для объекта LinkEntity
     * с тем же URL-адресом, что и URL-адрес входной ссылки. Затем он удаляет соответствующий объект
     * ChatLinkEntity из chatLinkEntityRepository, и если для ссылки больше нет ассоциаций чат-ссылки,
     * он удаляет ссылку из linkEntityRepository. Наконец, он использует
     * объект рефакторинга для преобразования полученного объекта LinkEntity в объект LinkResponse
     */
    @Transactional
    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        TgChatEntity tgChatEntity = tgChatEntityRepository.findByTgChatId(tgChatId)
            .orElseThrow(() -> new NotFoundException("Чат '" + tgChatId + "' не найден"));
        LinkEntity linkEntity = linkEntityRepository.findByLink(url.toString())
            .orElseThrow(() -> new NotFoundException("Ссылка '" + url + "' не найдена"));
        chatLinkEntityRepository.deleteByTgChatAndLink(tgChatEntity, linkEntity);
        if (chatLinkEntityRepository.getTgChatsByLinkId(linkEntity.getId()).size() == 0) {
            linkEntityRepository.deleteById(linkEntity.getId());
        }
        try {
            return refactor.linkEntityToLinkResponse(linkEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * этот метод принимает в качестве входных данных объект Long,
     * представляющий идентификатор чата Telegram, и объект URI, представляющий URL-адрес ссылки,
     * и добавляет связь между чатом и ссылкой в базу данных. Он делает это, запрашивая tgChatEntityRepository
     * для объекта TgChatEntity с тем же идентификатором, что и входной идентификатор чата,
     * и запрашивая linkEntityRepository для объекта LinkEntity с тем же URL-адресом, что и URL-адрес входной
     * ссылки. Если ссылка не существует в базе данных,он создает новый объект LinkEntity, используя объект linkRenew,
     * и сохраняет его в linkEntityRepository.Затем он создает новый объект ChatLinkEntity с извлеченными объектами
     * чата и ссылок и сохраняет его в chatLinkEntityRepository. Наконец, он использует объект рефакторинга
     * для преобразования полученного объекта LinkEntity в объект LinkResponse.
     */
    @Transactional
    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        TgChatEntity tgChatEntity = tgChatEntityRepository.findByTgChatId(tgChatId)
            .orElseThrow(() -> new NotFoundException("Чат '" + tgChatId + "' не найден"));
        LinkEntity linkEntity = linkEntityRepository.findByLink(url.toString()).orElseGet(
            () -> linkEntityRepository.save(linkRenew.createLinkEntity(url)));
        if (chatLinkEntityRepository.findByTgChatAndLink(tgChatEntity, linkEntity).isPresent()) {
            throw new RuntimeException("Ссылка '" + url + "' уже отслеживается чатом '" + tgChatId + "'");
        }
        ChatLinkEntity chatLinkEntity = new ChatLinkEntity();
        chatLinkEntity.setTgChat(tgChatEntity);
        chatLinkEntity.setLink(linkEntity);
        chatLinkEntityRepository.save(chatLinkEntity);
        try {
            return refactor.linkEntityToLinkResponse(linkEntity);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * этот метод принимает в качестве входных данных объект Long, представляющий идентификатор чата Telegram,
     * и возвращает список объектов Link, связанных с чатом. Он делает это, запрашивая в chatLinkEntityRepository
     * объекты ChatLinkEntity, которые имеют тот же идентификатор чата, что и входные данные, а затем использует
     * объект рефакторинга для преобразования полученных объектов LinkEntity в объекты Link.
     */
    @Transactional
    @Override
    public ListLinksResponse listAll(Long tgChatId) {
        if (tgChatEntityRepository.findByTgChatId(tgChatId).isEmpty()) {
            throw new NotFoundException("Чат '" + tgChatId + "' не найден");
        }
        return refactor.linkEntitiesToListLinksResponse(chatLinkEntityRepository.getLinksByTgChatId(tgChatId));
    }

    /**
     * этот метод возвращает список объектов Link, которые необходимо обновить. Он делает это,
     * запрашивая linkEntityRepository для всех объектов LinkEntity, фильтруя их на основе условия,
     * которое проверяет, находится ли их поле lastUpdate до определенного временного интервала,
     * а затем использует объект рефакторинга для сопоставления.
     */
    @Transactional
    @Override
    public List<Link> findLinksForUpdate() {
        return linkEntityRepository.findAll().stream().filter((LinkEntity le) ->
            le.getLastUpdate().isBefore(OffsetDateTime.of(
                LocalDateTime.now().minusMinutes(config.getUpdateInterval()),
                ZoneOffset.UTC
            ))
        ).map((LinkEntity le) -> {
            try {
                return refactor.linkEntityToLink(le);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    /**
     * этот метод принимает объект Link в качестве входных данных и обновляет соответствующий объект ссылки
     * в базе данных новыми данными. Это делается путем запроса в linkEntityRepository объекта LinkEntity
     * с тем же идентификатором, что и у входного идентификатора ссылки, а затем обновления его полей данными
     * из входного объекта Link. Наконец, он сохраняет обновленный объект ссылки в linkEntityRepository
     */
    @Transactional
    @Override
    public void updateLink(Link link) {
        LinkEntity linkEntity = linkEntityRepository.findById(link.getId())
            .orElseThrow(() -> new NotFoundException("Ссылка '" + link.getLink() + "' не найдена"));
        linkEntity.setAnswerCount(link.getAnswerCount());
        linkEntity.setLastUpdate(link.getLastUpdate());
        linkEntity.setLastActivity(link.getLastActivity());
        linkEntity.setOpenIssuesCount(link.getOpenIssuesCount());
        linkEntityRepository.save(linkEntity);
    }
}
