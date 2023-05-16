package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.configuration.access.AccessType;

@Getter
@Setter
@ToString
@Validated
public class ApplicationConfig {
    @NotNull
    String test;

    @NotNull
    Scheduler scheduler;

    @NotNull
    AccessType databaseAccessType;

    @NotNull
    Integer updateInterval;

    @NotNull
    boolean useQueue;

    String queueName;

    String exchangeName;
}

