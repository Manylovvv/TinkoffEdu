package ru.tinkoff.edu.java.scrapper.schemas.response;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {}
