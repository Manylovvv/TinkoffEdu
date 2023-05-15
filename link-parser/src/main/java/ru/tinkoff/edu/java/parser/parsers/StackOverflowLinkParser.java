package ru.tinkoff.edu.java.parser.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tinkoff.edu.java.parser.record.StackOverflowRecord;

public final class StackOverflowLinkParser extends AbstractLinkParser {
    private final Pattern pattern =
        Pattern.compile("^https://stackoverflow.com/questions/(\\d+)/[a-z-\\d]+");

    public StackOverflowLinkParser(AbstractLinkParser nextLink) {
        super(nextLink);
    }

    @Override
    public Record parseLink(String link) {
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return new StackOverflowRecord(Long.parseLong(matcher.group(1)));
        }
        if (nextParser != null) {
            return nextParser.parseLink(link);
        }
        return null;
    }
}
