package ru.tinkoff.edu.java.parser;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.parser.data.GitHubLinkData;
import ru.tinkoff.edu.java.parser.data.LinkData;
import ru.tinkoff.edu.java.parser.data.StackOverflowLinkData;
import ru.tinkoff.edu.java.parser.parsers.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ParserTest
{
    private static AbstractLinkParser Parser;

    @BeforeAll
    static void initParserChain()
    {
        Parser = new GitHubLinkParser();
        //chain of responsibility
        Parser.setNext(new StackOverflowLinkParser()).setNext(new NullLinkParser());
    }

    @Test
    void stackOverflowParseTest() {
        StackOverflowLinkData expected = new StackOverflowLinkData("1642028");
        LinkData actual = Parser.parseLink("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        assertEquals(expected , actual);
    }

    @Test
    public void gitHubParseTest() {
        LinkData expected = new GitHubLinkData("Manylovvv", "TinkoffEdu");
        LinkData actual = Parser.parseLink("https://github.com/Manylovvv/TinkoffEdu");
        assertEquals(expected, actual);
    }

    @Test
    void unsupportedStackOverflowLinkParseTest()
    {
        LinkData actual = Parser.parseLink("https://stackoverflow.com/search?q=unsupported%20link");
        assertEquals(null , actual);
    }


    @Test
    public void incorrectStackOverflowLinkParseTest() {
        var actual = Parser.parseLink("https://stackoverflow.com/questions");
        assertEquals(null, actual);
    }

    @Test
    public void nullParseTest() {
        assertNull(Parser.parseLink(null));
    }

    @Test
    public void incorrectGitHubLinkParseTest() {
        var actual = Parser.parseLink("https://github.com/Manylovvv");
        assertEquals(null, actual);
    }
}