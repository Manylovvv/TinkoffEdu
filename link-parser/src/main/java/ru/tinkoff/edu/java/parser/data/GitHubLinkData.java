package ru.tinkoff.edu.java.parser.data;

public record GitHubLinkData(String user_name, String repository_name) implements LinkData {
    @Override
    public String toString() {
        return user_name + "/" + repository_name;

    }

}