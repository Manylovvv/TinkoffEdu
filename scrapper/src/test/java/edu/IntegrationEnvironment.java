package edu;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class IntegrationEnvironment {
    static final protected PostgreSQLContainer POSTGRE_SQL_CONTAINER;
    static final private String CHANGELOG_PATH = "master.xml";
    static final private String POSTGRESQL_IMAGE = "postgres:14";
    static final private Path ROOT_DIRECTORY = Path.of(".").toAbsolutePath().getParent().getParent()
            .resolve("migrations/");

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(POSTGRESQL_IMAGE);
        POSTGRE_SQL_CONTAINER.start();

        try {
            Connection connection = DriverManager.getConnection(
                    POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                    POSTGRE_SQL_CONTAINER.getUsername(),
                    POSTGRE_SQL_CONTAINER.getPassword());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase(
                    CHANGELOG_PATH,
                    new DirectoryResourceAccessor(ROOT_DIRECTORY),
                    database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | LiquibaseException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}