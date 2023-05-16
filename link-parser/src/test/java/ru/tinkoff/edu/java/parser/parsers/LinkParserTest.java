package ru.tinkoff.edu.java.parser.parsers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.parser.records.StackOverflowRecord;

public class LinkParserTest {
    private final StackOverflowLinkParser stackOverflowLinkParser = new StackOverflowLinkParser(null);
    private final GitHubLinkParser gitHubLinkParser = new GitHubLinkParser(null);
    private final String username = "Manylovvv/";
    private final String repo = "TestRep";


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
        String link1 = "https://github.com/" + username;
        String link2 = "https://github.com/" + username + "/" + repo + "/" + "pulls";

        Record record1 = gitHubLinkParser.parseLink(link1);
        Record record2 = gitHubLinkParser.parseLink(link2);

        Assertions.assertNull(record1);
        Assertions.assertNull(record2);

        Record record = stackOverflowLinkParser.parseLink(link);

        Assertions.assertNull(record);
    }
}
