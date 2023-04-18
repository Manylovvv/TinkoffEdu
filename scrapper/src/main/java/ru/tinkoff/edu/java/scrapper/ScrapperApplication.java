package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

@SpringBootApplication
@EnableScheduling
public class ScrapperApplication
{

    public static void main( String[] args )
    {
        var ctx = SpringApplication.run(ScrapperApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        System.out.println(config);
    }

    @Bean("applicationConfig")
    @ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
    public ApplicationConfig applicationConfig() {
        return new ApplicationConfig();
    }
}
