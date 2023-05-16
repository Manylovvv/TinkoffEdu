package ru.tinkoff.edu.java.parser.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tinkoff.edu.java.parser.records.StackOverflowRecord;

public final class StackOverflowLinkParser extends AbstractLinkParser {
    private static final String PATTERN_STRING = "^https://stackoverflow.com/questions/(\\d+)/[a-z-\\d]+";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);
    public StackOverflowLinkParser(AbstractLinkParser nextLink) {
        super(nextLink);
    }
    @Override
    public Record parseLink(String link) {
        Matcher matcher = PATTERN.matcher(link);
        if (matcher.find()) {
            return new StackOverflowRecord(Long.parseLong(matcher.group(1)));
        }
        return nextParser != null ? nextParser.parseLink(link) : null;
    }
}
