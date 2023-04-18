package ru.tinkoff.edu.java.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.parser.parsers.StackOverflowLinkParser;
import ru.tinkoff.edu.java.parser.record.StackOverflowRecord;

public class StackOverflowLinkParserTest {
    private final StackOverflowLinkParser stackOverflowLinkParser = new StackOverflowLinkParser(null);

    @Test
    public void validLinkParse() {
        long questionId = 1642028L;
        String link = "https://stackoverflow.com/questions/" + questionId + "/what-is-the-operator-in-c";

        Record record = stackOverflowLinkParser.parseLink(link);

        Assertions.assertNotNull(record);
        Assertions.assertTrue(record instanceof StackOverflowRecord);
        Assertions.assertEquals(questionId, ((StackOverflowRecord)record).questionId());
    }

    @Test
    public void invalidLinkParse() {
        String link = "https://stackoverflow.com/search?q=unsupported%20link";

        Record record = stackOverflowLinkParser.parseLink(link);

        Assertions.assertNull(record);
    }
}
