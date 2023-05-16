package ru.tinkoff.edu.java.scrapper.service.converter;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.domain.repository.dto.Link;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinksResponse;


import java.util.List;

@Component
public class Converter {
    public LinkResponse linkToLinkResponse(Link link) {
        return new LinkResponse(link.getId(), link.getLink());
    }

    public ListLinksResponse linksToListLinksResponse(List<Link> links) {
        return new ListLinksResponse(links.stream().map(this::linkToLinkResponse).toList(), links.size());
    }
}
