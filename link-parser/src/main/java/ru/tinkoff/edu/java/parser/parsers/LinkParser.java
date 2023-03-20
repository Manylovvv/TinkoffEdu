package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.data.LinkData;

public sealed interface LinkParser permits AbstractLinkParser, NullLinkParser, GitHubLinkParser, StackOverflowLinkParser {
    LinkData parseLink(String site_url);
}