package ru.tinkoff.edu.java.scrapper.controller.dto.request;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveLinkRequest {
    private URI url;
}
