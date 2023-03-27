package ru.tinkoff.edu.java.scrapper.controller.response;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {}
