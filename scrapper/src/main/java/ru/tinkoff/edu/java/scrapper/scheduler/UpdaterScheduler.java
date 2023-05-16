package ru.tinkoff.edu.java.scrapper.scheduler;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.records.GitHubRecord;
import ru.tinkoff.edu.java.parser.records.StackOverflowRecord;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;
import ru.tinkoff.edu.java.scrapper.service.renew.LinkRenew;
import ru.tinkoff.edu.java.scrapper.service.updater.GitHubUpdater;
import ru.tinkoff.edu.java.scrapper.service.updater.StackOverflowUpdater;

@Component
@AllArgsConstructor
public class UpdaterScheduler {
    private final LinkService linkService;
    private final LinkRenew linkRenew;
    private final GitHubUpdater gitHubUpdater;
    private final StackOverflowUpdater stackOverflowUpdater;

    @Scheduled(fixedDelayString = "#{@applicationConfig.scheduler.interval()}")
    public void update() {
        List<Link> links = linkService.findLinksForUpdate();
        for (Link link: links) {
            Record apiRecord = linkRenew.getRecord(link);
            if (apiRecord instanceof GitHubRecord) {
                gitHubUpdater.update(link);
            } else if (apiRecord instanceof StackOverflowRecord) {
                stackOverflowUpdater.update(link);
            }
        }
    }
}
