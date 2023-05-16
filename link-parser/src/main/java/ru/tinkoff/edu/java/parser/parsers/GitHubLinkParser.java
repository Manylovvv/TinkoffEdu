package ru.tinkoff.edu.java.parser.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tinkoff.edu.java.parser.records.GitHubRecord;

public final class GitHubLinkParser extends AbstractLinkParser {
    private static final String GITHUB_LINK_REGEX = "^https://github.com/([\\w.-]+)/([\\w.-]+)/";
    public GitHubLinkParser(AbstractLinkParser nextParser) {
        super(nextParser);
    }
    @Override
    public Record parseLink(String link) {
        Matcher matcher = Pattern.compile(GITHUB_LINK_REGEX).matcher(link);
        if (matcher.matches()) {
            String username = matcher.group(1);
            String repositoryName = matcher.group(2);
            return new GitHubRecord(username, repositoryName);
        } else if (nextParser != null) {
            return nextParser.parseLink(link);
        } else {
            return null;
        }
    }
}
