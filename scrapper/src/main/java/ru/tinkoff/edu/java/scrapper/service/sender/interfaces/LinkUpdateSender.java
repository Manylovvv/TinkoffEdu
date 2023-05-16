package ru.tinkoff.edu.java.scrapper.service.sender.interfaces;

import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;

public interface LinkUpdateSender {
    void sendUpdate(Link link, String description);
}
