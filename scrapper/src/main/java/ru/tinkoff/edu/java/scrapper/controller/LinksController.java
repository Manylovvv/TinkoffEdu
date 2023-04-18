package ru.tinkoff.edu.java.scrapper.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;


@AllArgsConstructor
@RestController()
@RequestMapping("/links")
public class LinksController {
    private final JdbcLinkService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ListLinksResponse getTrackedLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        return service.listAll(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public LinkResponse addTrackedLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody AddLinkRequest request) {
        return service.add(id, request.getUrl());
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public LinkResponse deleteTrackedLink(@RequestHeader("Tg-Chat-Id") Long id, @RequestBody RemoveLinkRequest request) {
        return service.remove(id, request.getUrl());
    }
}
