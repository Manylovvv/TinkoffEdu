package ru.tinkoff.edu.java.parser.parsers;


import ru.tinkoff.edu.java.parser.data.LinkData;

public sealed interface LinkParser permits AbstractLinkParser, NullLinkParser, GitHubLinkParser, StackOverflowLinkParser, VkLinkParser {
    LinkData parseLink(String site_url);
}