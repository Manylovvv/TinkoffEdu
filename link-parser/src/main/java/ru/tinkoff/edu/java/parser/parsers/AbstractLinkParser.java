package ru.tinkoff.edu.java.parser.parsers;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract sealed class AbstractLinkParser permits GitHubLinkParser, StackOverflowLinkParser {
    protected AbstractLinkParser nextParser;

    abstract Record parseLink(String link);
}
