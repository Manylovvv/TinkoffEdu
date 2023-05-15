package ru.tinkoff.edu.java.parser.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tinkoff.edu.java.parser.record.GitHubRecord;

public final class GitHubLinkParser extends AbstractLinkParser {
    private final Pattern pattern = Pattern.compile("^https://github.com/([\\w.-]+)/([\\w.-]+)/");

    public GitHubLinkParser(AbstractLinkParser nextParser) {
        super(nextParser);
    }

    @Override
    public Record parseLink(String link) {
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return new GitHubRecord(matcher.group(1), matcher.group(2));
        }
        if (nextParser != null) {
            return nextParser.parseLink(link);
        }
        return null;
    }
}
