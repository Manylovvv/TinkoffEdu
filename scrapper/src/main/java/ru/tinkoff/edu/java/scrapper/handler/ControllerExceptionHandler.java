package ru.tinkoff.edu.java.scrapper.handler;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.scrapper.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.exception.ResourceNotFoundException;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse resourceNotFoundException(ResourceNotFoundException ex) {
        return new ApiErrorResponse(
                "Resource not found",
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
                "Invalid request parameters",
                "400",
                ex.getClass().getName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()));
    }
}
