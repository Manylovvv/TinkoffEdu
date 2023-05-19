package ru.tinkoff.edu.java.scrapper.controller.dto.request;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
/**
 * Генерирует параметризованный конструктор, который принимает
 * один параметр для каждого поля и инициализирует их с его помощью
 */
@AllArgsConstructor
@NoArgsConstructor
public class AddLinkRequest {
    private URI url;
}
