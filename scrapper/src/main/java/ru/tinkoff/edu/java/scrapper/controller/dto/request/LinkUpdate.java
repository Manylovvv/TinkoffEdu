package ru.tinkoff.edu.java.scrapper.controller.dto.request;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdate {
    private Long id;
    private URI url;
    private String description;
    private List<Long> tgChatIds;
}
