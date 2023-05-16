package ru.tinkoff.edu.java.scrapper.service.Updater;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.domain.repository.response.RepositoryResponse;
import ru.tinkoff.edu.java.parser.record.GitHubRecord;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkUpdater;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Service
public class GitHubLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final LinkManipulator linkManipulator;
    private final LinkUpdateSender linkUpdateSender;

    @Override
    public void update(Link link) {
        RepositoryResponse response = linkManipulator.getResponse((GitHubRecord) linkManipulator.getRecord(link));
        link.setLastUpdate(OffsetDateTime.now());
        if (!response.open_issues_count().equals(link.getOpenIssuesCount())) {
            String desc = (response.open_issues_count() > link.getOpenIssuesCount()) ?
                    "In repository '" + response.full_name() + "' new open issues have been added!" :
                    "In repository '" + response.full_name() + "' issues have been closed!";
            link.setLastActivity(response.updated_at());
            link.setOpenIssuesCount(response.open_issues_count());
            linkUpdateSender.sendUpdate(link, desc);
        } else if (response.updated_at().isAfter(link.getLastActivity())) {
            link.setLastActivity(response.updated_at());
            linkUpdateSender.sendUpdate(link, "Repository '" + response.full_name() + "' has updates!");
        }
        linkService.updateLink(link);
    }
}
