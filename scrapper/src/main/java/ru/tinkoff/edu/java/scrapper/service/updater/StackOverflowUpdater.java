package ru.tinkoff.edu.java.scrapper.service.updater;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.records.StackOverflowRecord;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.QuestionResponse;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;
import ru.tinkoff.edu.java.scrapper.service.sender.interfaces.LinkUpdateSender;

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
/**показывает, что класс представляет собой сервис для реализации бизнес-логики*/
@Service
public class StackOverflowUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final LinkRenew linkRenew;
    private final LinkUpdateSender linkUpdateSender;

    /**
     * Метод update использует объект linkRenew для получения последней информации о ссылке из Stack Overflow,
     * сравнивает количество ответов и дату последнего действия с текущими значениями в объекте ссылки и при
     * необходимости отправляет обновление в linkUpdateSender. Обновление содержит сообщение, которое зависит от того,
     * был ли добавлен новый ответ или вопрос был обновлен. Наконец, linkService используется для обновления
     * объекта ссылки в базе данных.
     */
    @Override
    public void update(Link link) {
        QuestionResponse response = linkRenew.getResponse((StackOverflowRecord) linkRenew.getRecord(link));
        link.setLastUpdate(OffsetDateTime.now(ZoneId.systemDefault()));
        if (!response.answer_count().equals(link.getAnswerCount())) {
            link.setLastActivity(response.last_activity_date());
            link.setAnswerCount(response.answer_count());
            linkUpdateSender.sendUpdate(
                link,
                "В вопросе '" + response.question_id() + "' был добавлен новый ответ"
            );
        } else if (response.last_activity_date().isAfter(link.getLastActivity())) {
            link.setLastActivity(response.last_activity_date());
            linkUpdateSender.sendUpdate(link, "Вопрос '" + response.question_id() + "' обновлен");
        }
        linkService.updateLink(link);
    }
}
