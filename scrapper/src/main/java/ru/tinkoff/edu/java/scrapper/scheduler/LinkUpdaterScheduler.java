package ru.tinkoff.edu.java.scrapper.scheduler;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.record.GitHubRecord;
import ru.tinkoff.edu.java.parser.record.StackOverflowRecord;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.service.Updater.GitHubLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.Updater.LinkManipulator;
import ru.tinkoff.edu.java.scrapper.service.Updater.StackOverflowLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;

@Component
@AllArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkService linkService;
    private final LinkManipulator linkManipulator;
    private final GitHubLinkUpdater gitHubLinkUpdater;
    private final StackOverflowLinkUpdater stackOverflowLinkUpdater;

    @Scheduled(fixedDelayString = "#{@applicationConfig.scheduler.interval()}")
    public void update() {
        List<Link> links = linkService.findLinksForUpdate();
        for (Link link: links) {
            Record apiRecord = linkManipulator.getRecord(link);
            if (apiRecord instanceof GitHubRecord) {
                gitHubLinkUpdater.update(link);
            } else if (apiRecord instanceof StackOverflowRecord) {
                stackOverflowLinkUpdater.update(link);
            }
        }
    }
}
