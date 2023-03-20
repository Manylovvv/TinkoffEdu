package ru.tinkoff.edu.java.parser.parsers;


import ru.tinkoff.edu.java.parser.data.GitHubLinkData;
import ru.tinkoff.edu.java.parser.data.LinkData;

public final class GitHubLinkParser extends AbstractLinkParser implements LinkParser {
    private static final String GITHUB_HOST = "github.com";
    @Override
    public LinkData parseLink(String url) {
        if (url.contains(GITHUB_HOST)) {
            String[] parts = url.split("/");
            if (parts.length >= 5) {
                String user_name = parts[3];
                String repository_name = parts[4];
                return new GitHubLinkData(user_name, repository_name);
            }
        }
        return handleNext(url);
    }
}