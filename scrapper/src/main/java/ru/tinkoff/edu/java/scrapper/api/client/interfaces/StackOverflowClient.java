package ru.tinkoff.edu.java.scrapper.api.client.interfaces;

import ru.tinkoff.edu.java.parser.data.StackOverflowLinkData;
import ru.tinkoff.edu.java.scrapper.api.response.stackoverflow.StackOverflowItemApiResponse;

public interface StackOverflowClient {
    StackOverflowItemApiResponse GetQuestion(StackOverflowLinkData question);
}

