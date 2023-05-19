package ru.tinkoff.edu.java.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * этот класс снабжен аннотациями @Validated и @ConfigurationProperties и определяет
 * свойства конфигурации для приложения. У него есть конструктор с аннотациями @NotNull
 * и @JsonProperty для каждого поля, где @NotNull указывает, что поле не может быть пустым,
 * а @JsonProperty указывает имя поля в файле конфигурации. Атрибут ignoreUnknownFields
 * @ConfigurationProperties имеет значение false, что означает, что будет создано исключение,
 * если в файле конфигурации будет найдено неизвестное поле.
 */
@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, @NotNull String token, @NotNull boolean useQueue,
                                String queueName, String exchangeName) {}
