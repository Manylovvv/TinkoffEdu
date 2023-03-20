package ru.tinkoff.edu.java.parser.data;

public record VkLinkData(String profile_id) implements LinkData {
    @Override
    public String toString() {
        return profile_id;
    }

}
