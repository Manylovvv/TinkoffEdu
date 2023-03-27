package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.controller.requests.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.controller.requests.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.controller.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.controller.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.exception.ResourceNotFoundException;

import java.util.*;

@RestController
@RequestMapping("/links")
public class LinkController {

    private final Map<Long, ListLinksResponse> ListLinks = new HashMap<>();
    private final Random RandomValue = new Random();

    public LinkController() {
        ListLinks.put(123456L, new ListLinksResponse(new ArrayList<>(
                Arrays.asList(new LinkResponse
                        (1, "https://github.com/Manylovvv/TinkoffEdu"))), 1));
        ListLinks.put(654321L, new ListLinksResponse
                (new ArrayList<>(Arrays.asList(new LinkResponse(2, "https://stackoverflow.com/questions/64828589/what-is-the-meaning-of-generating-sources-in-java-building-process"))), 1));
        ListLinks.put(567890L, new ListLinksResponse(new ArrayList<>(
                Arrays.asList(new LinkResponse(3, "https://stackoverflow.com/questions/2469911/how-do-i-assert-my-exception-message-with-junit-test-annotation"))), 1));
    }

    //Получить все отслеживаемые ссылки
    @GetMapping
    public ListLinksResponse getLink(@RequestHeader ("Tg-Chat-Id") long tgChatId){
        var listLinkResponse = ListLinks.get(tgChatId);
        if (listLinkResponse != null) return listLinkResponse;
        else throw new IllegalArgumentException(String.format("don`t exist this links for tg-chat-id", tgChatId));
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader ("Tg-Chat-Id") long tgChatId, @RequestBody AddLinkRequest request){
        var listLinksResponse = ListLinks.get(tgChatId);
        var newLinkResponse = new LinkResponse(RandomValue.nextLong(), request.link());
        if (listLinksResponse == null) {
            ListLinks.put(tgChatId, new ListLinksResponse(new ArrayList<>(Arrays.asList(newLinkResponse)), 1));
        } else {
            if (listLinksResponse.links().stream().anyMatch(linkResponse -> linkResponse.url().equals(newLinkResponse.url()))) {
                throw new IllegalArgumentException("The link you are trying to add already exists");
            } else {
                listLinksResponse.links().add(newLinkResponse);
                ListLinks.put(tgChatId, new ListLinksResponse(listLinksResponse.links(), listLinksResponse.size() + 1));
            }
        }
        return newLinkResponse;
    }

    @DeleteMapping
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") long tgChatId, @RequestBody RemoveLinkRequest request) {
        if (!ListLinks.containsKey(tgChatId)) {
            throw new IllegalArgumentException(String.format("There is no such tg-chat-id [%s]", tgChatId));
        }
        var listLinksResponse = ListLinks.get(tgChatId);
        if (!listLinksResponse.links().removeIf(x -> x.url().equals(request.link()))) {
            throw new ResourceNotFoundException(String.format("There is no such link [%s]", request.link()));
        } else {
            ListLinks.put(tgChatId, new ListLinksResponse(listLinksResponse.links(), listLinksResponse.size() - 1));
            return new LinkResponse(RandomValue.nextLong(), request.link());
        }
    }
}
