package ru.tinkoff.edu.java.scrapper.controller.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private String description;
    private String code;
    private String exception;
    private String message;
    private List<String> trace;
}
