package ru.tinkoff.edu.java.scrapper.configuration.sheduler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.convert.DurationUnit;

/**
 * Объявляет запись record с именем Scheduler, которая принимает параметр interval
 * типа Duration и аннотирована аннотацией @DurationUnit,
 * указывающей, что единицей измерения для interval являются миллисекунды (ChronoUnit.MILLIS).
 */
public record Scheduler(@DurationUnit(ChronoUnit.MILLIS) Duration interval) {}
