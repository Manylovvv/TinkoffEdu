package ru.tinkoff.edu.java.scrapper.configuration.jooq;

import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**Аннотация, которая определяет класс конфигурационным и содержит бины*/
@Configuration
public class JooqConfiguration {
    /**Аннотация, указывающая, что Spring должен внедрить зависимость класса DataSource в данном классе*/
    @Autowired
    DataSource dataSource;

    /**
     * Аннотация для методов в конфигурационном файле Спринга.
     * Она указывает на то, что метод должен быть оберткой над объектом-бином Спринга
     * Метод Используется для установки соединения с базой данных
     */
    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(
            new TransactionAwareDataSourceProxy(dataSource));
    }

    /**
     * Аннотация для методов в конфигурационном файле Спринга.
     * Она указывает на то, что метод должен быть оберткой над объектом-бином Спринга
     */
    @Bean
    public DSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    /**
     * В методе происходит Установка connectionProvider() в качестве провайдера соединения для config,
     * Установка диалекта SQL для работы с базой данных, Установка настроек для генерации SQL-запросов.
     * В данном случае, все имена таблиц и полей будут приведены к нижнему регистру,
     * установка слушателя, который будет обрабатывать исключения, возникающие при выполнении SQL-запросов
     */
    public DefaultConfiguration configuration() {
        DefaultConfiguration config = new DefaultConfiguration();
        config.set(connectionProvider());
        config.set(SQLDialect.POSTGRES);
        config.set(new Settings()
            .withRenderNameCase(RenderNameCase.LOWER));
        config.set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()));
        return config;
    }
}
