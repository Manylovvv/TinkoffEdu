package ru.tinkoff.edu.java.scrapper.handler;

import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.edu.java.scrapper.controller.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.exception.IncorrectArgumentException;
import ru.tinkoff.edu.java.scrapper.exception.ResourceNotFoundException;

import java.util.Arrays;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private ApiErrorResponse HandleOutput(String message, Exception exception, HttpStatus httpStatus) {
        return new ApiErrorResponse(
                message,
                String.valueOf(httpStatus.value()),
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(String::valueOf).toList()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse ResourceNotFoundException(NullPointerException Exception) {
        return HandleOutput("Null!!!", Exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse ResourceNotFoundException(ResourceNotFoundException Exception) {
        return HandleOutput("resource not found", Exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse IncorrectRequestParamsException(IncorrectArgumentException Exception) {
        return HandleOutput("This parameters is not correct", Exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse IncorrectParamsException(MethodArgumentTypeMismatchException Exception) {
        return HandleOutput("This parameters is not correct", Exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse IncorrectHeader(MissingRequestHeaderException Exception) {
        return HandleOutput("Required request header 'Tg-Chat-Id' for method parameter type long is not present", Exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse IncorrectRequest(HttpRequestMethodNotSupportedException Exception) {
        return HandleOutput("METOD NOT ALLOWED!", Exception, HttpStatus.BAD_REQUEST);
    }

}
