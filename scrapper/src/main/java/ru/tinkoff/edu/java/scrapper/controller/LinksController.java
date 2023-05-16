package ru.tinkoff.edu.java.scrapper.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.controller.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.controller.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkService;

@AllArgsConstructor
@RestController
@RequestMapping("/links")
public class LinksController {
    private final LinkService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public LinkResponse addTrackedLink(@RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody AddLinkRequest request) {
        return service.add(id, request.getUrl());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ListLinksResponse getTrackedLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        return service.listAll(id);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public LinkResponse deleteTrackedLink(@RequestHeader("Tg-Chat-Id") Long id,
        @RequestBody RemoveLinkRequest request) {
        return service.remove(id, request.getUrl());
    }
}
