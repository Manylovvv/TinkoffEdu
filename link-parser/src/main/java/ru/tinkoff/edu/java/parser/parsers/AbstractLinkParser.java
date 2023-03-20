package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.data.LinkData;

public abstract non-sealed class AbstractLinkParser implements LinkParser {
    private AbstractLinkParser next_parser;
    public AbstractLinkParser setNext(AbstractLinkParser next_parser) {
        this.next_parser = next_parser;
        return next_parser;
    }
    public LinkData handleNext(String url) {
        if (next_parser != null) {
            return next_parser.parseLink(url);
        }
        return null;
    }
}


