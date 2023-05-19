package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

/**Аннотация указывает что данный класс является приложением SpringBoot*/
@SpringBootApplication
/**Включение планирования в приложении Spring*/
@EnableScheduling
public class ScrapperApplication {

    //Точка входа приложения
    public static void main(String[] args) {

        SpringApplication.run(ScrapperApplication.class, args);
    }

    /**
     * Bean используется для создания bean-компонента типа ApplicationConfig
     * Аннотация @ConfigurationProperties используется для сопоставления свойств
     * конфигурации из файла свойств приложения с классом ApplicationConfig,
     * ignoreUnknownFields указывает, следует ли игнорировать неизвестные свойства.
     */
    @Bean("applicationConfig")
    @ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
    public ApplicationConfig applicationConfig() {
        return new ApplicationConfig();
    }
}
