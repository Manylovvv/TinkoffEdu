package ru.tinkoff.edu.java.scrapper.excontroller;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.scrapper.controller.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.excontroller.exception.NotFoundException;

@RestControllerAdvice
public class ControllerException {

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse resourceNotFoundException(NotFoundException ex) {
        return new ApiErrorResponse(
            "Не найдено",
            "404",
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
    }

    @ExceptionHandler(value = {
        MissingRequestHeaderException.class,
        HttpMessageNotReadableException.class,
        TypeMismatchException.class,
        MethodArgumentNotValidException.class  // in case valid checks are added
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse resourceNotFoundException(Exception ex) {
        return new ApiErrorResponse(
            "Неправильные параметры запроса",
            "400",
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
    }
}
