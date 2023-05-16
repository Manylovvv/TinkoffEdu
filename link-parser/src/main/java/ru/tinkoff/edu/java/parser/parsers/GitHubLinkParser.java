package ru.tinkoff.edu.java.parser.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tinkoff.edu.java.parser.records.GitHubRecord;

public final class GitHubLinkParser extends AbstractLinkParser {
    private static final String GITHUB_LINK_REGEX = "^https://github.com/([\\w.-]+)/([\\w.-]+)/";
    private static final Pattern PATTERN = Pattern.compile(GITHUB_LINK_REGEX);

    public GitHubLinkParser(AbstractLinkParser nextParser) {
        super(nextParser);
    }

    @Override
    public Record parseLink(String link) {
        Matcher matcher = PATTERN.matcher(link);
        if (matcher.find()) {
            String username = matcher.group(1);
            String repositoryName = matcher.group(2);
            return new GitHubRecord(username, repositoryName);
        }
        return nextParser != null ? nextParser.parseLink(link) : null;
    }
}
