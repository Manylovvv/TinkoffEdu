package ru.tinkoff.edu.java.scrapper.domain.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.domain.repository.jpa.entity.LinkEntity;

public interface LinkEntityRepository extends JpaRepository<LinkEntity, Long> {
    /**
     * метод findByLink, который возвращает необязательный
     *  объект LinkEntity, соответствующий заданной строке
     */
    Optional<LinkEntity> findByLink(String link);
}
