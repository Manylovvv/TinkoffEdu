package ru.tinkoff.edu.java.parser.parsers;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public sealed abstract class LinkParser permits GitHubLinkParser, StackOverflowLinkParser {
    protected LinkParser nextParser;

    abstract Record parseLink(String link);
}
