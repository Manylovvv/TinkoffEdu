package ru.tinkoff.edu.java.scrapper.service.interfaces;

import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;

public interface LinkUpdater {
    void update(Link link);
}
