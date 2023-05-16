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

@AllArgsConstructor
@Service
public class GitHubUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final LinkRenew linkRenew;
    private final LinkUpdateSender linkUpdateSender;

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
