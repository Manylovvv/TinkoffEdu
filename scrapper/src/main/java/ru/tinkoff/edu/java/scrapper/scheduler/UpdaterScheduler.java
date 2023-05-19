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

/**Аннотация, которая указывает класс в качестве Спринг-компонента*/
@Component
/**
 * Аннотация, которая генерирует параметризованный конструктор,
 * который принимает один параметр для каждого поля и инициализирует их с его помощью.
 */
@AllArgsConstructor
public class UpdaterScheduler {
    private final LinkService linkService;
    private final LinkRenew linkRenew;
    private final GitHubUpdater gitHubUpdater;
    private final StackOverflowUpdater stackOverflowUpdater;

    /**
     * Аннотация @Scheduled указывает, что метод update()
     * должен выполняться с фиксированными интервалами, определяемыми значением,
     * возвращаемым методом applicationConfig.scheduler.interval()
     *
     * Метод извлекает список ссылок, которые необходимо обновить,
     * используя метод findLinksForUpdate() службы LinkService
     * Затем он перебирает каждую ссылку и извлекает объект Record,
     * используя метод getRecord() LinkRenew
     * Если объект Record является экземпляром GitHubRecord, он вызывает
     * метод update() GitHubUpdater со ссылкой в качестве параметра.
     * Если объект Record является экземпляром StackOverflowRecord, он вызывает
     * метод update() объекта StackOverflowUpdater со ссылкой в качестве параметра
     */
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
