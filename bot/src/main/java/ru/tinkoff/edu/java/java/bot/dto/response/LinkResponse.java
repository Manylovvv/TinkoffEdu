package ru.tinkoff.edu.java.java.bot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkResponse {
    private Long id;
    private URI url;
}
