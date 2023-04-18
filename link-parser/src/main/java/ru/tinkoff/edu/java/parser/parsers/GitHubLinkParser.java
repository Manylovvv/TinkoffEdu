package ru.tinkoff.edu.java.parser.parsers;

import ru.tinkoff.edu.java.parser.record.GitHubRecord;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GitHubLinkParser extends LinkParser {
    private final Pattern PATTERN = Pattern.compile("^https://github.com/([\\w.-]+)/([\\w.-]+)/");

    public GitHubLinkParser(LinkParser nextParser) {
        super(nextParser);
    }

    @Override
    public Record parseLink(String link) {
        Matcher matcher = PATTERN.matcher(link);
        if (matcher.matches()) {
            return new GitHubRecord(matcher.group(1), matcher.group(2));
        }
        if (nextParser != null) {
            return nextParser.parseLink(link);
        }
        return null;
    }
}
