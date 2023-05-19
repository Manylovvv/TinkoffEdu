package ru.tinkoff.edu.java.scrapper.service.updater;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.records.GitHubRecord;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.RepositoryResponse;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;
import ru.tinkoff.edu.java.scrapper.service.sender.interfaces.LinkUpdateSender;

/**аннотация из библиотеки Lombok, которая генерирует конструктор со всеми аргументами*/
@AllArgsConstructor
/**показывает, что класс представляет собой сервис для реализации бизнес-логики*/
@Service
public class GitHubUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final LinkRenew linkRenew;
    private final LinkUpdateSender linkUpdateSender;

    /**
     * Метод обновления использует объект linkRenew для получения последней информации о ссылке из GitHub,
     * сравнивает количество открытых задач и дату последней активности с текущими значениями в объекте ссылки
     * и при необходимости отправляет обновление в linkUpdateSender. Обновление содержит сообщение, которое
     * зависит от того, была ли открыта или закрыта новая проблема или был ли обновлен репозиторий. Наконец,
     * linkService используется для обновления объекта ссылки в базе данных
     */
    @Override
    public void update(Link link) {
        RepositoryResponse response = linkRenew.getResponse((GitHubRecord) linkRenew.getRecord(link));
        link.setLastUpdate(OffsetDateTime.now());
        if (!response.open_issues_count().equals(link.getOpenIssuesCount())) {
            String desc = (response.open_issues_count() > link.getOpenIssuesCount())
                ? "В репозитории '" + response.full_name() + "' открыт новый реквест"
                : "В репозитории '" + response.full_name() + "' реквест закрыт";
            link.setLastActivity(response.updated_at());
            link.setOpenIssuesCount(response.open_issues_count());
            linkUpdateSender.sendUpdate(link, desc);
        } else if (response.updated_at().isAfter(link.getLastActivity())) {
            link.setLastActivity(response.updated_at());
            linkUpdateSender.sendUpdate(link, "Репозиторий'" + response.full_name() + "' обновлен");
        }
        linkService.updateLink(link);
    }
}
