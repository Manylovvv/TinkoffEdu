package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.configuration.access.enums.AccessType;
import ru.tinkoff.edu.java.scrapper.configuration.sheduler.Scheduler;

@Getter
@Setter
@ToString
@Validated
public class ApplicationConfig {
    @NotNull
    String test;
    @NotNull
    Integer updateInterval;
    @NotNull
    Scheduler scheduler;
    @NotNull
    AccessType databaseAccessType;
    @NotNull
    boolean useQueue;
    String exchangeName;
    String queueName;
}

