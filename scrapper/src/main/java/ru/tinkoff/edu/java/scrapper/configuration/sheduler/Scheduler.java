package ru.tinkoff.edu.java.scrapper.configuration.sheduler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.convert.DurationUnit;

public record Scheduler(@DurationUnit(ChronoUnit.MILLIS) Duration interval) { }
