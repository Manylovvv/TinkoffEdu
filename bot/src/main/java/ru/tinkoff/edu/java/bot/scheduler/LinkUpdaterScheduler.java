package ru.tinkoff.edu.java.bot.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class LinkUpdaterScheduler {
    private static final Logger LOG = LogManager.getLogger(LinkUpdaterScheduler.class);
    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        LOG.info("hello bot cache");
    }
}