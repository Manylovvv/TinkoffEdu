package ru.tinkoff.edu.java.scrapper.tests.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.scrapper.abstracts.IntegrationEnvironment;

public class IntegrationTest extends IntegrationEnvironment {

    @Test
    public void test() {
        Assertions.assertTrue(POSTGRE_SQL_CONTAINER.isCreated());
    }
}
