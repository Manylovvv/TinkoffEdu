package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.configuration.access.enums.AccessType;
import ru.tinkoff.edu.java.scrapper.configuration.sheduler.Scheduler;

/**
 * Аннотации @Getter и @Setter генерируют методы получения и установки для полей класса.
 * Аннотация @ToString генерирует метод toString() для класса, который возвращает строковое представление объекта.
 * Аннотация @Validated используется для проверки полей класса.
 * Назначение этого класса — хранить информацию о конфигурации приложения
 */
@Getter
@Setter
@ToString
@Validated
public class ApplicationConfig {
    /**
     * Строковое поле, в котором хранятся данные для теста (первые практики).
     */
    @NotNull
    String test;
    /**
     * Целочисленное поле, в котором хранится интервал обновления.
     */
    @NotNull
    Integer updateInterval;
    /**
     * поле, представляющее собой объект, планирующий выполнение задач через определенные промежутки времени.
     */
    @NotNull
    Scheduler scheduler;
    /**
     * поле перечисления AccessType, указывающее тип доступа к базе данных.
     */
    @NotNull
    AccessType databaseAccessType;
    /**
     * логическое поле, указывающее, следует ли использовать очередь.
     */
    @NotNull
    boolean useQueue;
    /**
     * строковое поле, в котором хранится имя обмена сообщениями.
     */
    String exchangeName;
    /**
     * строковое поле, в котором хранится имя очереди сообщений.
     */
    String queueName;
}

