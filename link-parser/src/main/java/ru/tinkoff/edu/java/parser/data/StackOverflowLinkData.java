package ru.tinkoff.edu.java.parser.data;

public record StackOverflowLinkData(String question_id) implements LinkData {
    @Override
    public String toString() {
        return question_id;
    }
}
