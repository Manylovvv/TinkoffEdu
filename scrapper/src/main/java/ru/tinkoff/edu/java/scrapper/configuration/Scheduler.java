package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public record Scheduler(@DurationUnit(ChronoUnit.MILLIS) Duration interval) { }
