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

@AllArgsConstructor
@Service
public class StackOverflowUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final LinkRenew linkRenew;
    private final LinkUpdateSender linkUpdateSender;

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
