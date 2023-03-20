package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.data.LinkData;

public final class NullLinkParser extends AbstractLinkParser implements LinkParser {
    @Override
    public LinkData parseLink(String site_url) {
        return null;
    }
}
