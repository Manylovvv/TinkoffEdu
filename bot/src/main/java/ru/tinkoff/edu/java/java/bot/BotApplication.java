package ru.tinkoff.edu.java.java.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.java.bot.configuration.ApplicationConfig;


@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication
{

    public static void main( String[] args )
    {
        SpringApplication.run(BotApplication.class, args);
    }
}
