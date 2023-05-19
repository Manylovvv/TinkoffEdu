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

/**Аннотация используется для указания того, что этот класс обеспечивает обработку исключений для всех контроллеров.*/
@RestControllerAdvice
public class ControllerException {

    /**
     * @ExceptionHandler. Первый метод обрабатывает исключения типа NotFoundException.
     * Он возвращает объект ApiErrorResponse с сообщением, кодом ошибки, именем класса исключения,
     * сообщением об исключении и трассировкой стека. Сообщение и код ошибки указывают на то,
     * что запрошенный ресурс не найден, и из исключения собирается трассировка стека.
     * Метод также устанавливает статус ответа HttpStatus.NOT_FOUND.
     */
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

    /**
     * Второй метод обрабатывает исключения нескольких типов, включая MissingRequestHeaderException,
     * HttpMessageNotReadableException, TypeMismatchException и MethodArgumentNotValidException.
     * Он возвращает объект ApiErrorResponse с сообщением, кодом ошибки, именем класса исключения,
     * сообщением об исключении и трассировкой стека. Сообщение и код ошибки указывают на то,
     * что возникла проблема с параметрами запроса, и трассировка стека собирается из исключения.
     * Метод также устанавливает статус ответа на HttpStatus.BAD_REQUEST.
     */
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
